package gr.uoa.di.digibid.web.controller;

import com.google.common.collect.Lists;

import gr.uoa.di.digibid.model.WebBid;
import gr.uoa.di.digibid.model.WebItem;
import gr.uoa.di.digibid.model.WebItemImage;
import gr.uoa.di.digibid.model.WebLocation;
import gr.uoa.di.digibid.persist.converter.LocalDateTimeConverter;
import gr.uoa.di.digibid.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.groupingBy;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Controller
public class ItemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemCategoryService itemCategoryService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemImageService itemImageService;

    @Autowired
    private BidService bidService;

    @Autowired
    private LocationService locationService;

    @RequestMapping("/items")
    public ModelAndView getAllItems(@PageableDefault Pageable pageable) {

        Page<WebItem> items = itemService.getAllItems(pageable);
        Map<Long, List<WebItemImage>> itemImages = items.getContent().stream().flatMap(webItem -> itemImageService.getItemImageByItemId(webItem.getId()).stream()).collect(groupingBy(WebItemImage::getWebItemId));

        LOGGER.debug("Getting {} items for page {}", items.getContent().size(), pageable.getPageNumber());

        ModelAndView modelAndView = new ModelAndView("items", "items", Lists.newArrayList(items.getContent()));
        modelAndView.addObject("itemCategories", itemCategoryService.getAllItemCategories());
        modelAndView.addObject("locations", locationService.getAllLocations().stream().map(WebLocation::getName).collect(Collectors.toCollection(LinkedHashSet::new)));
        modelAndView.addObject("prices", calculatePrices());
        modelAndView.addObject("itemImages", itemImages);
        modelAndView.addObject("totalPages", items.getTotalPages());
        modelAndView.addObject("totalItems", items.getTotalElements());
        modelAndView.addObject("currentPage", pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber() + 1);

        return modelAndView;
    }

    @RequestMapping("/items/search")
    public ModelAndView getSearchResults(@RequestParam("q") String query,
                                         @RequestParam("c") List<Long> categories,
                                         @RequestParam("l") List<String> locations,
                                         @RequestParam("p") List<String> prices,
                                         @PageableDefault Pageable pageable) {

        Page<WebItem> items = itemService.search(pageable, query, categories, locations, prices, false);
        Map<Long, List<WebItemImage>> itemImages = items.getContent().stream().flatMap(webItem -> itemImageService.getItemImageByItemId(webItem.getId()).stream()).collect(groupingBy(WebItemImage::getWebItemId));

        LOGGER.debug("Getting {} items for page {}, query={}, categories={}, locations={}, prices={}", items.getContent().size(), pageable.getPageNumber(), query, categories, locations, prices);

        ModelAndView modelAndView = new ModelAndView("items", "items", items.getContent());
        modelAndView.addObject("itemCategories", itemCategoryService.getAllItemCategories());
        modelAndView.addObject("locations", locationService.getAllLocations().stream().map(WebLocation::getName).collect(Collectors.toCollection(LinkedHashSet::new)));
        modelAndView.addObject("prices", calculatePrices());
        modelAndView.addObject("itemImages", itemImages);
        modelAndView.addObject("totalPages", items.getTotalPages());
        modelAndView.addObject("totalItems", items.getTotalElements());
        modelAndView.addObject("currentPage", pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber() + 1);
        modelAndView.addObject("q", query);
        modelAndView.addObject("c", categories);

        return modelAndView;
    }

    @RequestMapping("/item/{id}")
    public ModelAndView getItem(@PathVariable Long id) {

        Optional<WebItem> item = itemService.getItemById(id);

        List<WebBid> webBids = Lists.newArrayList();
        if (!item.isPresent()) {
            return new ModelAndView("error", "itemCategories", itemCategoryService.getAllItemCategories());
        } else {
            webBids.addAll(bidService.getBidsByItemId(item.get().getId()));
        }

        WebItem webItem = item.orElseGet(WebItem::new);

        Map<Long, List<WebItemImage>> itemImages = itemImageService.getItemImageByItemId(item.get().getId()).stream().collect(groupingBy(WebItemImage::getWebItemId));

        LOGGER.debug("Getting item {}", id);

        ModelAndView modelAndView = new ModelAndView("item", "item", webItem);
        modelAndView.addObject("itemCategories", itemCategoryService.getAllItemCategories());
        modelAndView.addObject("itemImages", itemImages);
        modelAndView.addObject("webBids", webBids);

        return modelAndView;
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #username)")
    @RequestMapping("/items/user/{username}")
    public ModelAndView getSellerUserItems(@PathVariable String username, @PageableDefault Pageable pageable) {

        Page<WebItem> items = itemService.getItemsBySeller(username, pageable);
        Map<Long, List<WebItemImage>> itemImages = items.getContent().stream().flatMap(webItem -> itemImageService.getItemImageByItemId(webItem.getId()).stream()).collect(groupingBy(WebItemImage::getWebItemId));

        LOGGER.debug("Getting {} items for page {} and user={}", items.getContent().size(), pageable.getPageNumber(), username);

        ModelAndView modelAndView = new ModelAndView("items", "items", items.getContent());
        modelAndView.addObject("itemCategories", itemCategoryService.getAllItemCategories());
        modelAndView.addObject("locations", emptySet());
        modelAndView.addObject("prices", emptySet());
        modelAndView.addObject("itemImages", itemImages);
        modelAndView.addObject("totalPages", items.getTotalPages());
        modelAndView.addObject("totalItems", items.getTotalElements());
        modelAndView.addObject("currentPage", pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber() + 1);

        return modelAndView;
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #username)")
    @RequestMapping("/items/user/{username}/history")
    public ModelAndView getBidderUserItems(@PathVariable String username, @PageableDefault Pageable pageable) {

        Page<WebItem> items = itemService.getItemsByBidder(username, pageable);
        Map<Long, List<WebItemImage>> itemImages = items.getContent().stream().flatMap(webItem -> itemImageService.getItemImageByItemId(webItem.getId()).stream()).collect(groupingBy(WebItemImage::getWebItemId));

        LOGGER.debug("Getting {} bidding items for page {} and user={}", items.getContent().size(), pageable.getPageNumber(), username);

        ModelAndView modelAndView = new ModelAndView("items", "items", items.getContent());
        modelAndView.addObject("itemCategories", itemCategoryService.getAllItemCategories());
        modelAndView.addObject("locations", emptySet());
        modelAndView.addObject("prices", emptySet());
        modelAndView.addObject("itemImages", itemImages);
        modelAndView.addObject("totalPages", items.getTotalPages());
        modelAndView.addObject("totalItems", items.getTotalElements());
        modelAndView.addObject("currentPage", pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber() + 1);

        return modelAndView;
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #username)")
    @RequestMapping(value = "/items/user/{username}/new", method = RequestMethod.GET)
    public ModelAndView getUserNewItem(@PathVariable String username) {

        LOGGER.debug("Getting new item page for user={}", username);

        ModelAndView modelAndView = new ModelAndView("items_new", "itemCategories", itemCategoryService.getAllItemCategories());
        modelAndView.addObject("webItem", new WebItem());

        return modelAndView;
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #username)")
    @RequestMapping(value = "/items/user/{username}/new", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<?> handleNewItemForm(@PathVariable String username, @Valid @ModelAttribute("webItem") WebItem webItem, BindingResult bindingResult) {

        LOGGER.debug("Processing item create form={}, bindingResult={}", webItem, bindingResult);

        if (bindingResult.hasErrors()) {
            LOGGER.error("{}", bindingResult.getAllErrors());
            // failed validation

            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            webItem.setStarted(""); //activation sets the started date

            MultipartFile imageFile = webItem.getImageFile();
            LocalDateTime endsLocalDateTime = LocalDateTimeConverter.parseSimple(webItem.getEnds());
            webItem.setEnds(LocalDateTimeConverter.toFormattedString(endsLocalDateTime));
            webItem.setSellerUsername(username);
            webItem = itemService.createOrUpdate(webItem);

            if (!imageFile.isEmpty()) {
                WebItemImage webItemImage = WebItemImage.builder()
                        .webItem(webItem)
                        .dataInBytes(imageFile.getBytes())
                        .description(webItem.getName())
                        .build();
                itemImageService.createOrUpdate(webItemImage);
            }
        } catch (IOException e) {
            LOGGER.error("{}", e.getMessage(), e);

            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Exception occurred when trying to save the item", e);

            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PreAuthorize("@currentUserServiceImpl.userOwnsTheItem(principal, #id)")
    @RequestMapping(value = "/item/{id}/edit", method = RequestMethod.GET)
    public ModelAndView getItemEdit(@PathVariable Long id) {

        LOGGER.debug("Getting item {} edit page", id);

        WebItem webItem = itemService.getItemById(id).get();

        String dateFormatted = LocalDateTimeConverter.toSimpleFormattedString(LocalDateTimeConverter.parse(webItem.getEnds()));
        webItem.setEnds(dateFormatted);

        ModelAndView modelAndView = new ModelAndView("items_edit", "itemCategories", itemCategoryService.getAllItemCategories());
        modelAndView.addObject("webItem", webItem);

        return modelAndView;
    }

    @PreAuthorize("@currentUserServiceImpl.userOwnsTheItem(principal, #id)")
    @RequestMapping(value = "/item/{id}/edit", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<?> handleEditItemForm(@PathVariable Long id, @Valid @ModelAttribute("webItem") WebItem webItem, BindingResult bindingResult) {

        LOGGER.debug("Processing item edit form={}, bindingResult={}", webItem, bindingResult);

        if (bindingResult.hasErrors()) {
            LOGGER.error("{}", bindingResult.getAllErrors());
            // failed validation

            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            webItem.setSellerUsername(auth.getName());
            LocalDateTime endsLocalDateTime = LocalDateTimeConverter.parseSimple(webItem.getEnds());
            webItem.setEnds(LocalDateTimeConverter.toFormattedString(endsLocalDateTime));

            itemService.createOrUpdate(webItem);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Exception occurred when trying to save the item", e);

            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Set<String> calculatePrices() {

        List<WebItem> items = itemService.getAllItems();
        Set<String> prices = new LinkedHashSet<>();

        if (CollectionUtils.isEmpty(items)) {
            return prices;
        }

        List<WebItem> sortedItems = items.stream().filter(webItem -> webItem.getPrice() != null).sorted(Comparator.comparing(webItem -> Long.valueOf(webItem.getPrice()))).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(sortedItems)) {
            return prices;
        }

        String priceInString = sortedItems.get(sortedItems.size() - 1).getPrice();

        if (StringUtils.isEmpty(priceInString)) {
            return prices;
        }

        int maxPrice = Integer.valueOf(priceInString);

        int diff = maxPrice / 4;

        int prev = 0;
        int curr = diff;
        for (int i = 0; i < 3; i++) {
            prices.add(prev + "-" + curr);
            prev = curr + 1;
            curr = curr + diff;
        }
        prices.add((prev + 1) + "-" + maxPrice);

        return prices;
    }
}
package gr.uoa.di.digibid.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import gr.uoa.di.digibid.model.WebCountry;
import gr.uoa.di.digibid.model.WebItem;
import gr.uoa.di.digibid.model.WebItemCategory;
import gr.uoa.di.digibid.model.WebMessage;
import gr.uoa.di.digibid.model.WebWrapper;
import gr.uoa.di.digibid.persist.converter.LocalDateTimeConverter;
import gr.uoa.di.digibid.service.BidService;
import gr.uoa.di.digibid.service.CountryService;
import gr.uoa.di.digibid.service.ItemCategoryService;
import gr.uoa.di.digibid.service.ItemService;
import gr.uoa.di.digibid.service.MessageService;
import gr.uoa.di.digibid.service.UserService;

/**
 * Created by amehrabyan, gpozidis on 05/09/16.
 */
@RestController
@RequestMapping(value = "/api")
public class RestApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiController.class);

    @Autowired
    private CountryService countryService;

    @Autowired
    private ItemCategoryService itemCategoryService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private BidService bidService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ResourceLoader resourceLoader;

    private static final ObjectMapper XML_MAPPER = new XmlMapper();

    @RequestMapping(value = "/countries/search", method = RequestMethod.GET)
    public List<WebCountry> searchCountries(@RequestParam("q") String query) {
        return countryService.search(query);
    }

    @RequestMapping(value = "/items/search", method = RequestMethod.GET)
    public List<WebItem> searchItems(@RequestParam("q") String query) {
        return itemService.search(new PageRequest(0, 10), query, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), true).getContent();
    }

    @RequestMapping(value = "/categories/search", method = RequestMethod.GET)
    public List<WebItemCategory> searchCategories(@RequestParam("q") String query) {
        return itemCategoryService.search(query);
    }

    @RequestMapping(value = "/countries", method = RequestMethod.GET)
    public List<WebCountry> getAllCountries() {
        return countryService.getAllCountries();
    }

    @PreAuthorize("@currentUserServiceImpl.canReadMessage(principal, #id)")
    @RequestMapping(value = "/message/{id}", method = RequestMethod.GET)
    public Boolean saveOrUpdateMessage(@PathVariable Long id) {
        Optional<WebMessage> webMessage = messageService.getMessageById(id);

        if (!webMessage.isPresent()) {
            LOGGER.error("Couldn't find message with the id {}", id);
            return false;
        }
        WebMessage webMessageObject = webMessage.get();
        if (!webMessageObject.isRead()) {
            webMessageObject.setRead(true);

            messageService.createOrUpdate(webMessageObject);

            LOGGER.debug("Message with the id {} marked as read", id);
        } else {
            LOGGER.debug("Message with the id {} is already marked as read", id);
        }

        return true;
    }

    @PreAuthorize("@currentUserServiceImpl.canReadMessage(principal, #id)")
    @RequestMapping(value = "/message/{id}/delete", method = RequestMethod.GET)
    public Boolean deleteMessage(@PathVariable Long id) {
        Optional<WebMessage> webMessage = messageService.getMessageById(id);

        if (!webMessage.isPresent()) {
            LOGGER.error("Couldn't find message with the id {}", id);
            return false;
        }
        WebMessage webMessageObject = webMessage.get();
        if (!webMessageObject.isDeleted()) {
            webMessageObject.setDeleted(true);

            messageService.createOrUpdate(webMessageObject);

            LOGGER.debug("Message with the id {} marked as deleted", id);
        } else {
            LOGGER.debug("Message with the id {} is already marked as deleted", id);
        }

        return true;
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #username)")
    @RequestMapping(value = "/user/{username}/messages/unread", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Long> getUnreadMessageCount(@PathVariable String username) {
        Long unreadMessagesCount = messageService.getUnreadMessageCountByReceiverUsername(username);

        LOGGER.debug("Found {} unread messages for user={} ", unreadMessagesCount, username);

        return ResponseEntity.ok(unreadMessagesCount);
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #username)")
    @RequestMapping(value = "/items/user/{username}/download", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable String username) {

        String fileName = username + "-items.xml";
        File file = new File(fileName);

        convertToXML(itemService.getItemsBySeller(username), file);

        Resource resource = new FileSystemResource(file);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PreAuthorize("@currentUserServiceImpl.userOwnsTheItem(principal, #id)")
    @RequestMapping(value = "/item/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable Long id) {

        LOGGER.debug("Deleting item={}", id);

        Boolean deleted = itemService.remove(id);
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("@currentUserServiceImpl.userOwnsTheItem(principal, #id)")
    @RequestMapping(value = "/item/{id}/activate", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> activate(@PathVariable Long id) {

        LOGGER.debug("Activating item={} ", id);

        Optional<WebItem> webItem = itemService.getItemById(id);

        if (!webItem.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        webItem.get().setStarted(LocalDateTimeConverter.toFormattedString(LocalDateTime.now()));

        itemService.createOrUpdate(webItem.get());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/items/download", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> downloadAll() {

        String fileName = "all-items.xml";
        File file = new File(fileName);

        convertToXML(itemService.getAllItems(), file);

        Resource resource = new FileSystemResource(file);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    private void convertToXML(List<WebItem> items, File file) {
        if (items.size() > 0) {
            try {
                WebWrapper<WebItem> webItems = new WebWrapper<>(items);
                XML_MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, webItems);
            } catch (IOException e) {
                LOGGER.error("{}", e.getMessage(), e);
            }
        }

        file.deleteOnExit();
    }
}
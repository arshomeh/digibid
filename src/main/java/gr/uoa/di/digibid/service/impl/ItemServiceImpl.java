package gr.uoa.di.digibid.service.impl;

import gr.uoa.di.digibid.model.WebItem;
import gr.uoa.di.digibid.persist.converter.DomainConverter;
import gr.uoa.di.digibid.persist.converter.LocalDateTimeConverter;
import gr.uoa.di.digibid.persist.domain.*;
import gr.uoa.di.digibid.persist.repository.*;
import gr.uoa.di.digibid.service.ItemService;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static gr.uoa.di.digibid.persist.converter.DomainConverter.convert;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ItemCategoryRepository itemCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<WebItem> getItemById(Long id) {
        Item item = itemRepository.findOne(id);
        return item != null ? Optional.ofNullable(convert(item)) : Optional.empty();
    }

    @Override
    public Page<WebItem> getItemsBySeller(String sellerUsername, Pageable pageable) {
        return itemRepository.findBySellerUsername(sellerUsername, pageable).map(DomainConverter::convert);
    }

    @Override
    public Page<WebItem> getItemsByBidder(String bidderUsername, Pageable pageable) {
        return itemRepository.findByBidderUsername(bidderUsername, pageable).map(DomainConverter::convert);
    }

    @Override
    public List<WebItem> getItemsBySeller(String sellerUsername) {
        return itemRepository.findBySellerUsername(sellerUsername).stream().map(DomainConverter::convert).collect(toList());
    }

    @Override
    public Page<WebItem> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable).map(DomainConverter::convert);
    }

    @Override
    public List<WebItem> getAllItems() {
        return itemRepository.findAll().stream().map(DomainConverter::convert).collect(toList());
    }

    @Override
    public Page<WebItem> search(Pageable pageable, String text, List<Long> categories, List<String> locations, List<String> prices, boolean autocomplete) {

        if (autocomplete) {
            List<WebItem> results = itemRepository.findByNameContaining(new Sort("name"), text).stream().map(DomainConverter::convert).collect(toList());
            return new PageImpl<>(results, pageable, results.size());
        }

        // get the full text entity manager
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Item.class).get();

        Query query = null;

        // a very basic query by keywords
        if (!StringUtils.isEmpty(text)) {
            query = queryBuilder.bool()
                    .should(queryBuilder.phrase().onField("name").sentence(text).createQuery())
                    .should(queryBuilder.phrase().onField("description").sentence(text).createQuery())
                    .createQuery();
        }

        if (!CollectionUtils.isEmpty(categories)) {
            if (query == null) {
                query = queryBuilder.bool()
                        .must(queryBuilder.keyword().onField("itemCategories.name").matching(categories.stream().map(categoryId -> itemCategoryRepository.getOne(categoryId).getName()).collect(Collectors.joining(" "))).createQuery())
                        .createQuery();
            } else {
                query = queryBuilder.bool()
                        .must(query)
                        .must(queryBuilder.keyword().onField("itemCategories.name").matching(categories.stream().map(categoryId -> itemCategoryRepository.getOne(categoryId).getName()).collect(Collectors.joining(" "))).createQuery())
                        .createQuery();
            }
        }

        if (!CollectionUtils.isEmpty(locations)) {
            if (query == null) {
                query = queryBuilder.bool()
                        .must(queryBuilder.keyword().onField("location.name").matching(locations.stream().collect(Collectors.joining(" "))).createQuery())
                        .createQuery();
            } else {
                query = queryBuilder.bool()
                        .must(query)
                        .must(queryBuilder.keyword().onField("location.name").matching(locations.stream().collect(Collectors.joining(" "))).createQuery())
                        .createQuery();
            }
        }

        if (!CollectionUtils.isEmpty(prices)) {


            List<Long> allPrices = prices.stream().flatMap(price -> Arrays.stream(price.split("-"))).map(Long::valueOf).sorted().collect(toList());

            long min = allPrices.get(0);
            long max = allPrices.get(allPrices.size() - 1);

            if (query == null) {
                query = queryBuilder.bool()
                        .must(queryBuilder.range().onField("price").above(min).createQuery())
                        .must(queryBuilder.range().onField("price").below(max).createQuery())
                        .createQuery();
            } else {
                query = queryBuilder.bool()
                        .must(query)
                        .must(queryBuilder.range().onField("price").above(min).createQuery())
                        .must(queryBuilder.range().onField("price").below(max).createQuery())
                        .createQuery();
            }
        }

        // wrap Lucene query in an Hibernate Query object
        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Item.class);

        fullTextQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()); //start from the 15th element
        fullTextQuery.setMaxResults(pageable.getPageSize());

        // execute search and return results (sorted by relevance as default)
        List<WebItem> webItems = (List<WebItem>) fullTextQuery.getResultList().stream().filter(object -> ((Item) object).getStarted() != null).map(object -> convert((Item) object)).collect(toList());

        return new PageImpl<>(webItems, pageable, fullTextQuery.getResultSize());
    }

    @Override
    public WebItem createOrUpdate(WebItem webItem) {
        return convert(itemRepository.saveAndFlush(prepareWebItem(webItem)));
    }

    @Override
    public Boolean remove(Long id) {

        itemRepository.delete(id);
        itemRepository.flush();
        Item item = itemRepository.findOne(id);

        if (item != null) {
            return false;
        }

        return true;
    }

    private Item prepareWebItem(WebItem webItem) {

        WebItem existingWebItem = webItem;

        if (webItem.getId() != null) {
            LOGGER.debug("Updating existing item");

            existingWebItem = convert(itemRepository.findOne(webItem.getId()));
            existingWebItem.setName(webItem.getName());
            existingWebItem.setFirstBid(webItem.getFirstBid());
            existingWebItem.setPrice(webItem.getPrice());
            existingWebItem.setDescription(webItem.getDescription());
            existingWebItem.setItemCategories(webItem.getItemCategories());
            existingWebItem.setCountryName(webItem.getCountryName());
            existingWebItem.setLocationName(webItem.getLocationName());
            existingWebItem.setLatitude(webItem.getLatitude());
            existingWebItem.setLongitude(webItem.getLongitude());

            if (!StringUtils.isEmpty(webItem.getStarted())) {
                existingWebItem.setStarted(webItem.getStarted());
            }

            LocalDateTime endsLocalDateTime = LocalDateTimeConverter.parse(webItem.getEnds());
            existingWebItem.setEnds(LocalDateTimeConverter.toFormattedString(endsLocalDateTime));
        } else {
            LOGGER.debug("Adding new item");
        }

        Optional<Country> country = countryRepository.findByName(existingWebItem.getCountryName());

        if (!country.isPresent()) {
            country = Optional.of(countryRepository.saveAndFlush(Country.builder().name(existingWebItem.getCountryName()).build()));
        }

        Optional<Location> location = locationRepository.findByNameAndByLatLon(existingWebItem.getLocationName(), existingWebItem.getLatitude(), existingWebItem.getLongitude());
        if (!location.isPresent()) {
            location = Optional.of(locationRepository.saveAndFlush(
                    Location.builder()
                            .name(webItem.getLocationName())
                            .latitude(webItem.getLatitude())
                            .longitude(webItem.getLongitude())
                            .country(country.get())
                            .build())
            );
        }

        Set<ItemCategory> itemCategories = existingWebItem.getItemCategories().stream().map(itemCategoryId -> itemCategoryRepository.findOne(itemCategoryId)).collect(toSet());
        Optional<User> seller = userRepository.findByUsername(existingWebItem.getSellerUsername());

        if (!CollectionUtils.isEmpty(existingWebItem.getWebBids())) {
            existingWebItem.setBids(existingWebItem.getWebBids().stream().map(bidId -> convert(bidRepository.getOne(bidId))).collect(toList()));
        }

        existingWebItem.setCountry(convert(country.get()));
        existingWebItem.setLocation(convert(location.get()));
        existingWebItem.setCategories(itemCategories.stream().map(DomainConverter::convert).collect(toSet()));
        existingWebItem.setSeller(convert(seller.get()));

        return convert(existingWebItem);
    }
}

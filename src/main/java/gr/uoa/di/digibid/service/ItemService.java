package gr.uoa.di.digibid.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import gr.uoa.di.digibid.model.WebItem;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
public interface ItemService {

    Optional<WebItem> getItemById(Long id);

    Page<WebItem> getItemsBySeller(String sellerUsername, Pageable pageable);

    Page<WebItem> getItemsByBidder(String bidderUsername, Pageable pageable);

    List<WebItem> getItemsBySeller(String sellerUsername);

    Page<WebItem> getAllItems(Pageable pageable);

    List<WebItem> getAllItems();

    Page<WebItem> search(Pageable pageable, String text, List<Long> categories, List<String> locations, List<String> prices, boolean autocomplete);

    WebItem createOrUpdate(WebItem webItem);

    Boolean remove(Long id);
}

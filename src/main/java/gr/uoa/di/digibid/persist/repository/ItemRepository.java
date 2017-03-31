package gr.uoa.di.digibid.persist.repository;

import gr.uoa.di.digibid.persist.domain.ItemCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import javax.transaction.Transactional;

import gr.uoa.di.digibid.persist.domain.Item;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Transactional
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select item from Item item where item.seller.username = ?1")
    Page<Item> findBySellerUsername(String sellerUsername, Pageable pageable);

    @Query("select item from Item item join item.bids bid where bid.bidder.username = ?1 group by item")
    Page<Item> findByBidderUsername(String bidderUsername, Pageable pageable);

    @Query("select item from Item item where item.seller.username = ?1")
    List<Item> findBySellerUsername(String sellerUsername);

    @Query("select item from Item item where item.started IS NOT NULL")
    Page<Item> findAll(Pageable pageable);

    List<Item> findAll();

    List<Item> findByNameContaining(Sort sort, String name);


}

package gr.uoa.di.digibid.persist.repository;

import gr.uoa.di.digibid.persist.domain.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Transactional
public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("select bid from Bid bid where bid.bidder.id = ?1")
    List<Bid> findByBidderId(Long bidderId);

    @Query("select bid from Bid bid where bid.item.id = ?1")
    List<Bid> findByItemId(Long itemId);
}
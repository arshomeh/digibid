package gr.uoa.di.digibid.service;

import gr.uoa.di.digibid.model.WebBid;

import java.util.List;
import java.util.Optional;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
public interface BidService {

    Optional<WebBid> getBidById(Long id);

    List<WebBid> getBidsByBidderId(Long bidderId);

    List<WebBid> getBidsByItemId(Long itemId);

    List<WebBid> getAllBids();

    WebBid createOrUpdate(WebBid webBid);
}

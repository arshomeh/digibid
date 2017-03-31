package gr.uoa.di.digibid.service.impl;

import gr.uoa.di.digibid.model.WebBid;
import gr.uoa.di.digibid.model.WebItem;
import gr.uoa.di.digibid.persist.converter.DomainConverter;
import gr.uoa.di.digibid.persist.domain.Bid;
import gr.uoa.di.digibid.persist.repository.BidRepository;
import gr.uoa.di.digibid.service.BidService;
import gr.uoa.di.digibid.service.ItemService;
import gr.uoa.di.digibid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Service
@Transactional
public class BidServiceImpl implements BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Override
    public Optional<WebBid> getBidById(Long id) {
        Bid bid = bidRepository.findOne(id);
        return bid != null ? Optional.ofNullable(DomainConverter.convert(bid)) : Optional.empty();
    }

    @Override
    public List<WebBid> getBidsByBidderId(Long bidderId) {
        return bidRepository.findByBidderId(bidderId).stream().map(DomainConverter::convert).collect(toList());
    }

    @Override
    public List<WebBid> getBidsByItemId(Long itemId) {
        return bidRepository.findByItemId(itemId).stream().map(DomainConverter::convert).collect(toList());
    }

    @Override
    public List<WebBid> getAllBids() {
        return bidRepository.findAll(new Sort("time")).stream().map(DomainConverter::convert).collect(toList());
    }

    @Override
    public WebBid createOrUpdate(WebBid webBid) {

        if (webBid.getWebItem() == null && webBid.getWebItemId() != null) {
            Optional<WebItem> item = itemService.getItemById(webBid.getWebItemId());
            webBid.setWebItem(item.orElseGet(WebItem::new));
        }

        if (!StringUtils.isEmpty(webBid.getBidderUsername())) {
            webBid.setBidder(userService.getUserByUsername(webBid.getBidderUsername()).get());
        }

        Bid bid = bidRepository.saveAndFlush(DomainConverter.convert(webBid));

        return DomainConverter.convert(bid);
    }
}

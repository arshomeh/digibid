package gr.uoa.di.digibid.web.controller;

import gr.uoa.di.digibid.model.WebBid;
import gr.uoa.di.digibid.model.WebItem;
import gr.uoa.di.digibid.model.WebUser;
import gr.uoa.di.digibid.persist.converter.LocalDateTimeConverter;
import gr.uoa.di.digibid.service.BidService;
import gr.uoa.di.digibid.service.ItemService;
import gr.uoa.di.digibid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by amehrabyan, gpozidis on 08/09/16.
 */
@Controller
public class BidController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private BidService bidService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Transactional
    @MessageMapping("/bid/place")
    public void receive(WebBid webBid) {

        webBid.setTime(LocalDateTimeConverter.toFormattedString(LocalDateTime.now()));
        WebBid bid = bidService.createOrUpdate(webBid);

        WebItem webItem = itemService.getItemById(webBid.getWebItemId()).get();

        if (webBid.getAmount() >= Long.valueOf(webItem.getPrice())) {
            userService.increaseSellerRating(webItem.getSellerUsername());
            userService.increaseBidderRating(webBid.getBidderUsername());
        }

        Map<String, Boolean> usersNotified = new HashMap<>();

        simpMessagingTemplate.convertAndSendToUser(webItem.getSeller().getUsername(), "/bid/receive", bid);

        usersNotified.put(webItem.getSeller().getUsername(), true);

        webItem.getWebBids().forEach(bidId -> {
            WebBid wb = bidService.getBidById(bidId).get();

            if (usersNotified.get(wb.getBidderUsername()) == null && !webBid.getBidderUsername().equalsIgnoreCase(wb.getBidderUsername())) {
                simpMessagingTemplate.convertAndSendToUser(wb.getBidderUsername(), "/bid/receive", bid);
                usersNotified.put(wb.getBidderUsername(), true);
            }
        });
    }
}

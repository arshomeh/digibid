package gr.uoa.di.digibid.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.function.Supplier;

import gr.uoa.di.digibid.exceptions.UserNotFoundException;
import gr.uoa.di.digibid.model.WebMessage;
import gr.uoa.di.digibid.persist.converter.LocalDateTimeConverter;
import gr.uoa.di.digibid.service.MessageService;
import gr.uoa.di.digibid.service.UserService;

/**
 * Created by amehrabyan, gpozidis on 08/09/16.
 */
@Controller
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/message/send")
    public void receive(WebMessage webMessage) throws UserNotFoundException {

        webMessage.setTo(userService.getUserByUsername(webMessage.getToUsername()).orElseThrow((Supplier<RuntimeException>) () -> new UserNotFoundException("Couldn't find user for " + webMessage.getToUsername())));
        webMessage.setFrom(userService.getUserByUsername(webMessage.getFromUsername()).orElseThrow((Supplier<RuntimeException>) () -> new UserNotFoundException("Couldn't find user for " + webMessage.getFromUsername())));

        webMessage.setTime(LocalDateTimeConverter.toFormattedString(LocalDateTime.now()));
        WebMessage message = messageService.createOrUpdate(webMessage);

        simpMessagingTemplate.convertAndSendToUser(webMessage.getTo().getUsername(), "/message/receive", message);
    }
}

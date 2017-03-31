package gr.uoa.di.digibid.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import gr.uoa.di.digibid.model.WebItem;
import gr.uoa.di.digibid.model.WebMessage;
import gr.uoa.di.digibid.model.WebMessageType;
import gr.uoa.di.digibid.model.WebUser;
import gr.uoa.di.digibid.service.ItemCategoryService;
import gr.uoa.di.digibid.service.MessageService;
import gr.uoa.di.digibid.service.UserService;
import gr.uoa.di.digibid.web.validator.WebUserValidator;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private WebUserValidator webUserValidator;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemCategoryService itemCategoryService;

    @Autowired
    private MessageService messageService;

    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(webUserValidator);
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #username)")
    @RequestMapping("/{username}")
    public ModelAndView getUserPage(@PathVariable String username) {

        LOGGER.debug("Getting user page for user={}", username);

        ModelAndView modelAndView = new ModelAndView("user", "user", userService.getUserByUsername(username).orElseThrow(() -> new NoSuchElementException(String.format("User=%s not found", username))));
        modelAndView.addObject("itemCategories", itemCategoryService.getAllItemCategories());

        return modelAndView;
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #username)")
    @RequestMapping("/{username}/inbox")
    public ModelAndView getUserInbox(@PathVariable String username) {

        LOGGER.debug("Getting user inbox page for user={}", username);

        Map<String, List<WebMessage>> messages = new HashMap<>();
        messages.put(WebMessageType.INCOMING.getName(), messageService.getMessagesByReceiverUsername(username));

        return new ModelAndView("messages", "messages", messages);
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #username)")
    @RequestMapping("/{username}/outbox")
    public ModelAndView getUserOutbox(@PathVariable String username) {

        LOGGER.debug("Getting user outbox page for user={}", username);

        Map<String, List<WebMessage>> messages = new HashMap<>();
        messages.put(WebMessageType.OUTGOING.getName(), messageService.getMessagesBySenderUsername(username));

        return new ModelAndView("messages", "messages", messages);
    }

    @PreAuthorize("@currentUserServiceImpl.canActivateUser(principal, #username)")
    @RequestMapping("/{username}/activate/{status}")
    public String activateUser(@PathVariable String username, @PathVariable Boolean status) {

        LOGGER.debug("Activating={} user={}", status, username);

        userService.activate(username, status);

        return "redirect:/users";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView getUserRegistrationPage() {

        LOGGER.debug("Getting user create form");

        ModelAndView modelAndView = new ModelAndView("register", "form", new WebUser());
        modelAndView.addObject("itemCategories", itemCategoryService.getAllItemCategories());

        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView handleUserRegistrationForm(@Valid @ModelAttribute("form") WebUser form, BindingResult bindingResult) {

        LOGGER.debug("Processing user create form={}, bindingResult={}", form, bindingResult);

        if (bindingResult.hasErrors()) {
            LOGGER.warn("{}", bindingResult.getAllErrors());
            // failed validation

            return new ModelAndView("register", "itemCategories", itemCategoryService.getAllItemCategories());
        }
        try {
            userService.createOrUpdate(form);
        } catch (DataIntegrityViolationException e) {
            LOGGER.warn("Exception occurred when trying to save the user", e);

            return new ModelAndView("register", "itemCategories", itemCategoryService.getAllItemCategories());
        }

        // ok, redirect
        ModelAndView modelAndView = new ModelAndView("index", "itemCategories", itemCategoryService.getAllItemCategories());
        modelAndView.addObject("submitted", true);

        return modelAndView;
    }

    @RequestMapping(value = "/register/submitted", method = RequestMethod.GET)
    public ModelAndView getUserRegistrationSubmittedPage() {
        return new ModelAndView("registration_request_submitted", "itemCategories", itemCategoryService.getAllItemCategories());
    }
}

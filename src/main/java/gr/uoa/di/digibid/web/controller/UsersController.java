package gr.uoa.di.digibid.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import gr.uoa.di.digibid.service.ItemCategoryService;
import gr.uoa.di.digibid.service.UserService;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Controller
public class UsersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ItemCategoryService itemCategoryService;

    @RequestMapping("/users")
    public ModelAndView getUsersPage() {

        LOGGER.debug("Getting users page");

        ModelAndView modelAndView = new ModelAndView("users", "users", userService.getAllUsers());
        modelAndView.addObject("itemCategories", itemCategoryService.getAllItemCategories());

        return modelAndView;
    }
}

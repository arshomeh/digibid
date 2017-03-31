package gr.uoa.di.digibid.web.controller;

import gr.uoa.di.digibid.service.ItemCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    public static final String DEFAULT_SUCCESS_URL = "/";
    public static final String ADMIN_SUCCESS_URL = "/users";

    @Autowired
    private ItemCategoryService itemCategoryService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLoginPage(@RequestParam Optional<String> error) {

        LOGGER.debug("Getting login page, error={}", error);

        ModelAndView modelAndView = new ModelAndView("login", "error", error);
        modelAndView.addObject("itemCategories", itemCategoryService.getAllItemCategories());

        return modelAndView;
    }
}
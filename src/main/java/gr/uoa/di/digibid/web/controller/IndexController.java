package gr.uoa.di.digibid.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import gr.uoa.di.digibid.service.ItemCategoryService;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Controller
public class IndexController implements ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private ItemCategoryService itemCategoryService;

    @RequestMapping("/")
    public ModelAndView getHomePage() {

        LOGGER.debug("Getting index page");

        ModelAndView modelAndView = new ModelAndView("index", "itemCategories", itemCategoryService.getAllItemCategories());
        modelAndView.addObject("submitted", false);

        return modelAndView;
    }

    @RequestMapping(value = "/error")
    public ModelAndView error() {
        return new ModelAndView("error", "itemCategories", itemCategoryService.getAllItemCategories());
    }

    @RequestMapping(value = "/help")
    public ModelAndView help() {
        return new ModelAndView("help", "itemCategories", itemCategoryService.getAllItemCategories());
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
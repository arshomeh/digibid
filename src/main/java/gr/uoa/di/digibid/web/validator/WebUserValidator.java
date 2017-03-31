package gr.uoa.di.digibid.web.validator;

import gr.uoa.di.digibid.model.WebUser;
import gr.uoa.di.digibid.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Component
public class WebUserValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebUserValidator.class);

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(WebUser.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        LOGGER.debug("Validating {}", target);

        WebUser form = (WebUser) target;
        validatePasswords(errors, form);
        validateEmail(errors, form);
        validateUsername(errors, form);
    }

    private void validatePasswords(Errors errors, WebUser webUser) {
        if (!webUser.getPassword().equals(webUser.getPasswordRepeated())) {
            LOGGER.error("Passwords do not match");
            errors.reject("password.no_match", "Passwords do not match");
        }
    }

    private void validateEmail(Errors errors, WebUser webUser) {
        if (userService.getUserByEmail(webUser.getEmail()).isPresent()) {
            LOGGER.error("User with the email {} already exists", webUser.getEmail());
            errors.reject("email.exists", "User with this email " + webUser.getEmail() + " already exists");
        }
    }

    private void validateUsername(Errors errors, WebUser webUser) {
        if (userService.getUserByUsername(webUser.getUsername()).isPresent()) {
            LOGGER.error("User with the username {} already exists", webUser.getUsername());
            errors.reject("username.exists", "User with this username " + webUser.getUsername() + " already exists");
        }
    }
}
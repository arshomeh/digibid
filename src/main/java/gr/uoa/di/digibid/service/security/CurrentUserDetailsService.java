package gr.uoa.di.digibid.service.security;

import gr.uoa.di.digibid.exceptions.UserNotActivatedException;
import gr.uoa.di.digibid.model.CurrentUser;
import gr.uoa.di.digibid.model.WebUser;
import gr.uoa.di.digibid.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Service
public class CurrentUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Override
    public CurrentUser loadUserByUsername(String username) throws AuthenticationException {

        LOGGER.debug("Authenticating user with username={}", username);

        WebUser webUser = userService.getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The username or password you have entered is invalid, try again."));

        if (!webUser.isActive()) {
            throw new UserNotActivatedException("Administrator has not yet approved your registration request, try again later.");
        }

        return new CurrentUser(webUser);
    }
}
package gr.uoa.di.digibid.service;

import gr.uoa.di.digibid.model.WebUser;

import java.util.List;
import java.util.Optional;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
public interface UserService {

    Optional<WebUser> getUserByEmail(String email);

    Optional<WebUser> getUserByUsername(String username);

    List<WebUser> getAllUsers();

    WebUser createOrUpdate(WebUser webUser);

    void activate(String username, Boolean activate);

    void increaseBidderRating(String username);

    void increaseSellerRating(String username);
}

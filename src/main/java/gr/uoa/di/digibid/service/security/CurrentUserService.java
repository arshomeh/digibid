package gr.uoa.di.digibid.service.security;

import gr.uoa.di.digibid.model.CurrentUser;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
public interface CurrentUserService {
    boolean canAccessUser(CurrentUser currentUser, String username);

    boolean canActivateUser(CurrentUser currentUser, String username);

    boolean canSendMessage(CurrentUser currentUser, String username);

    boolean canReadMessage(CurrentUser currentUser, Long message);

    boolean userOwnsTheItem(CurrentUser currentUser, Long id);
}
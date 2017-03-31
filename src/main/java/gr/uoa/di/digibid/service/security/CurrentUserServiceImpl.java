package gr.uoa.di.digibid.service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

import gr.uoa.di.digibid.model.CurrentUser;
import gr.uoa.di.digibid.model.WebItem;
import gr.uoa.di.digibid.model.WebMessage;
import gr.uoa.di.digibid.persist.domain.User;
import gr.uoa.di.digibid.service.ItemService;
import gr.uoa.di.digibid.service.MessageService;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserDetailsService.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private ItemService itemService;

    /**
     * Users with ADMIN role can access other users' profile pages and users' can access only their own profiel page.
     */
    @Override
    public boolean canAccessUser(CurrentUser currentUser, String username) {

        boolean hasAccess = currentUser != null && (!CollectionUtils.isEmpty(currentUser.getRoles()) && currentUser.getRoles().contains(User.Role.ADMIN.toString()) || currentUser.getUsername().equalsIgnoreCase(username));

        LOGGER.debug("Checking if user={} has access to user={}. {}", currentUser, username, hasAccess);

        return hasAccess;
    }

    /**
     * Only user with ADMIN role can activate other users.
     */
    @Override
    public boolean canActivateUser(CurrentUser currentUser, String username) {

        boolean hasAccess = currentUser != null && (!CollectionUtils.isEmpty(currentUser.getRoles()) && currentUser.getRoles().contains(User.Role.ADMIN.toString()) && !currentUser.getUsername().equalsIgnoreCase(username));

        LOGGER.debug("Checking if user={} has access to activate user={}. {}", currentUser, username, hasAccess);

        return hasAccess;
    }

    /**
     * Only users with BIDDER and SELLER roles can send messages to each other.
     */
    @Override
    public boolean canSendMessage(CurrentUser currentUser, String username) {

        boolean hasAccess = currentUser != null && (!CollectionUtils.isEmpty(currentUser.getRoles()) && currentUser.getRoles().contains(User.Role.BIDDER.toString()) || currentUser.getRoles().contains(User.Role.SELLER.toString()) || !currentUser.getUsername().equalsIgnoreCase(username));

        LOGGER.debug("Checking if user={} can send messages to user={}. {}", currentUser, username, hasAccess);

        return hasAccess;
    }

    @Override
    public boolean canReadMessage(CurrentUser currentUser, Long messageId) {

        Optional<WebMessage> webMessage = messageService.getMessageById(messageId);

        boolean hasAccess = webMessage.isPresent() && currentUser != null && !webMessage.get().isDeleted() && webMessage.get().getTo().getUsername().equals(currentUser.getUsername()) || webMessage.get().getFrom().getUsername().equalsIgnoreCase(currentUser.getUsername());

        LOGGER.debug("Checking if user={} can read message={}. {}", currentUser, messageId, hasAccess);

        return hasAccess;
    }

    @Override
    public boolean userOwnsTheItem(CurrentUser currentUser, Long id) {

        Optional<WebItem> webItem = itemService.getItemById(id);

        boolean hasAccess = currentUser != null && webItem.isPresent() && webItem.get().getSellerUsername().equalsIgnoreCase(currentUser.getUsername()) && (!CollectionUtils.isEmpty(currentUser.getRoles()) && !currentUser.getRoles().contains(User.Role.ADMIN.toString()) && currentUser.getRoles().contains(User.Role.SELLER.toString()));

        LOGGER.debug("Checking if user={} owns the item={}. {}", currentUser, id, hasAccess);

        return hasAccess;
    }
}
package gr.uoa.di.digibid.service;

import java.util.List;
import java.util.Optional;

import gr.uoa.di.digibid.model.WebMessage;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
public interface MessageService {

    Optional<WebMessage> getMessageById(Long id);

    Long getUnreadMessageCountByReceiverUsername(String receiverUsername);

    List<WebMessage> getMessagesBySenderUsername(String senderUsername);

    List<WebMessage> getMessagesByReceiverUsername(String receiverUsername);

    WebMessage createOrUpdate(WebMessage webMessage);

    void delete(Long id);
}

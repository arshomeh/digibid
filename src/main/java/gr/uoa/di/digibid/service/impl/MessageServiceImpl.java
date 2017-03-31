package gr.uoa.di.digibid.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import gr.uoa.di.digibid.model.WebMessage;
import gr.uoa.di.digibid.persist.converter.DomainConverter;
import gr.uoa.di.digibid.persist.domain.Message;
import gr.uoa.di.digibid.persist.repository.MessageRepository;
import gr.uoa.di.digibid.service.MessageService;

import static java.util.stream.Collectors.toList;

/**
 * Created by amehrabyan, gpozidis on 07/09/16.
 */
@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Optional<WebMessage> getMessageById(Long id) {

        LOGGER.debug("Getting message={}", id);

        Message message = messageRepository.findOne(id);
        return message != null ? Optional.ofNullable(DomainConverter.convert(message)) : Optional.empty();
    }

    @Override
    public Long getUnreadMessageCountByReceiverUsername(String receiverUsername) {

        LOGGER.debug("Getting unread message count by receiver={}", receiverUsername);

        return messageRepository.findUnreadMessageCountByReceiverUsername(receiverUsername);
    }

    @Override
    public List<WebMessage> getMessagesBySenderUsername(String senderUsername) {

        LOGGER.debug("Getting messages by sender={}", senderUsername);

        return messageRepository.findBySenderUsername(senderUsername).stream().map(DomainConverter::convert).collect(toList());
    }

    @Override
    public List<WebMessage> getMessagesByReceiverUsername(String receiverUsername) {

        LOGGER.debug("Getting messages by receiver={}", receiverUsername);

        return messageRepository.findByReceiverUsername(receiverUsername).stream().map(DomainConverter::convert).collect(toList());
    }

    @Override
    public WebMessage createOrUpdate(WebMessage webMessage) {
        LOGGER.debug("Created/updated new message={}", webMessage);

        return DomainConverter.convert(messageRepository.saveAndFlush(DomainConverter.convert(webMessage)));
    }

    @Override
    public void delete(Long id) {
        LOGGER.debug("Deleted message={}", id);
        messageRepository.delete(id);
    }
}

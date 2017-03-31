package gr.uoa.di.digibid.persist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import javax.transaction.Transactional;

import gr.uoa.di.digibid.persist.domain.Message;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Transactional
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select message from Message message where message.from.username = ?1 and message.deleted = 0")
    List<Message> findBySenderUsername(String senderUsername);

    @Query("select message from Message message where message.to.username = ?1 and message.deleted = 0")
    List<Message> findByReceiverUsername(String receiverUsername);

    @Query("select count(message) from Message message where message.viewed = 0 and message.deleted = 0 and message.to.username = ?1")
    Long findUnreadMessageCountByReceiverUsername(String receiverUsername);

    @Query("select message from Message message where message.deleted = 0")
    List<Message> findAll();

}
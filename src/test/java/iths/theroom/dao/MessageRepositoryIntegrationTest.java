package iths.theroom.dao;

import iths.theroom.entity.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class MessageRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void saveRead() {
        Message message = new Message(Message.Type.CHAT, "hello", "sven");
        entityManager.persist(message);
        assertThat(messageRepository.findById(message.getId()).orElseThrow(NoSuchElementException::new)).isEqualTo(message);
    }

    @Test
    public void saveWithNewInfoUpdates() {
        Message message = new Message(Message.Type.CHAT, "hello", "sven");
        entityManager.persist(message);
        Message foundMessage = messageRepository.findById(message.getId()).orElseThrow(NoSuchElementException::new);
        foundMessage.setContent("bye");
        messageRepository.save(message);
        assertThat(messageRepository.findById(message.getId()).orElseThrow(NoSuchElementException::new).getContent()).isEqualTo("bye");
    }

    @Test
    public void remove() {
        Message message = new Message(Message.Type.CHAT, "hello", "sven");
        entityManager.persist(message);
        Message foundMessage = messageRepository.findById(message.getId()).orElseThrow(NoSuchElementException::new);
        messageRepository.delete(foundMessage);
        assertThat(messageRepository.findById(message.getId()).isEmpty());
    }



}
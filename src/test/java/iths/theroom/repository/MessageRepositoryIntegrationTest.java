package iths.theroom.repository;


import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.enums.Type;
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
        MessageEntity message = new MessageEntity(Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(message);
        assertThat(messageRepository.findById(message.getId()).orElseThrow(NoSuchElementException::new)).isEqualTo(message);
    }
    @Test
    public void getByUuid() {
        MessageEntity message = new MessageEntity(Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(message);
        assertThat(messageRepository.findByUuid(message.getUuid()).orElseThrow(NoSuchElementException::new)).isEqualTo(message);
    }

    @Test
    public void getRoom() {
        MessageEntity message = new MessageEntity(Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(message);
        assertThat(messageRepository.findById(message.getId()).orElseThrow(NoSuchElementException::new).getRoomEntity().getRoomName()).isEqualTo("one");
    }

    @Test
    public void getSender() {
        MessageEntity message = new MessageEntity(Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(message);
        assertThat(messageRepository.findById(message.getId()).orElseThrow(NoSuchElementException::new).getSender().getUserName()).isEqualTo("sven");
    }


    @Test
    public void updateRoom() {
        MessageEntity message = new MessageEntity(Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(message);
        message.setRoomEntity(new RoomEntity("two"));
        entityManager.merge(message);
        assertThat(messageRepository.findById(message.getId()).orElseThrow(NoSuchElementException::new).getRoomEntity().getRoomName()).isEqualTo("two");
    }

    @Test
    public void updateSender() {
        MessageEntity message = new MessageEntity(Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(message);
        message.setSender(new UserEntity("johan"));
        entityManager.merge(message);
        assertThat(messageRepository.findById(message.getId()).orElseThrow(NoSuchElementException::new).getSender().getUserName()).isEqualTo("johan");
    }


    @Test
    public void getUserEntity() {
        MessageEntity message = new MessageEntity(Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(message);
        assertThat(messageRepository.findById(message.getId()).orElseThrow(NoSuchElementException::new).getSender().getUserName()).isEqualTo("sven");
    }


    @Test
    public void saveWithNewInfoUpdates() {
        MessageEntity message = new MessageEntity(Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(message);
        MessageEntity foundMessage = messageRepository.findById(message.getId()).orElseThrow(NoSuchElementException::new);
        foundMessage.setContent("bye");
        messageRepository.save(message);
        assertThat(messageRepository.findById(message.getId()).orElseThrow(NoSuchElementException::new).getContent()).isEqualTo("bye");
    }

    @Test
    public void remove() {
        MessageEntity message = new MessageEntity(Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(message);
        MessageEntity foundMessage = messageRepository.findById(message.getId()).orElseThrow(NoSuchElementException::new);
        messageRepository.delete(foundMessage);
        assertThat(messageRepository.findById(message.getId()).isEmpty());
    }



}

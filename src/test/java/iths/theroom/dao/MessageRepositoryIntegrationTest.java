package iths.theroom.dao;

import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.repository.MessageRepository;
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
        MessageEntity messageEntity = new MessageEntity(MessageEntity.Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(messageEntity);
        assertThat(messageRepository.findById(messageEntity.getId()).orElseThrow(NoSuchElementException::new)).isEqualTo(messageEntity);
    }
    @Test
    public void getByUuid() {
        MessageEntity messageEntity = new MessageEntity(MessageEntity.Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(messageEntity);
        assertThat(messageRepository.findByUuid(messageEntity.getUuid()).orElseThrow(NoSuchElementException::new)).isEqualTo(messageEntity);
    }

    @Test
    public void getRoom() {
        MessageEntity messageEntity = new MessageEntity(MessageEntity.Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(messageEntity);
        assertThat(messageRepository.findById(messageEntity.getId()).orElseThrow(NoSuchElementException::new).getRoomEntity().getRoomName()).isEqualTo("one");
    }

    @Test
    public void getSender() {
        MessageEntity messageEntity = new MessageEntity(MessageEntity.Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(messageEntity);
        assertThat(messageRepository.findById(messageEntity.getId()).orElseThrow(NoSuchElementException::new).getSender().getUserName()).isEqualTo("sven");
    }


    @Test
    public void updateRoom() {
        MessageEntity messageEntity = new MessageEntity(MessageEntity.Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(messageEntity);
        messageEntity.setRoomEntity(new RoomEntity("two"));
        entityManager.merge(messageEntity);
        assertThat(messageRepository.findById(messageEntity.getId()).orElseThrow(NoSuchElementException::new).getRoomEntity().getRoomName()).isEqualTo("two");
    }

    @Test
    public void updateSender() {
        MessageEntity messageEntity = new MessageEntity(MessageEntity.Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(messageEntity);
        messageEntity.setSender(new UserEntity("johan"));
        entityManager.merge(messageEntity);
        assertThat(messageRepository.findById(messageEntity.getId()).orElseThrow(NoSuchElementException::new).getSender().getUserName()).isEqualTo("johan");
    }


    @Test
    public void getUserEntity() {
        MessageEntity messageEntity = new MessageEntity(MessageEntity.Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(messageEntity);
        assertThat(messageRepository.findById(messageEntity.getId()).orElseThrow(NoSuchElementException::new).getSender().getUserName()).isEqualTo("sven");
    }

    @Test
    public void deleteSenderDoesNotDeleteRealSender() {
        //TODO
    }

    @Test
    public void deleteRoomDoesNotDeleteRealRoom() {
        //TODO
    }

    @Test
    public void saveWithNewInfoUpdates() {
        MessageEntity messageEntity = new MessageEntity(MessageEntity.Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(messageEntity);
        MessageEntity foundMessageEntity = messageRepository.findById(messageEntity.getId()).orElseThrow(NoSuchElementException::new);
        foundMessageEntity.setContent("bye");
        messageRepository.save(messageEntity);
        assertThat(messageRepository.findById(messageEntity.getId()).orElseThrow(NoSuchElementException::new).getContent()).isEqualTo("bye");
    }

    @Test
    public void remove() {
        MessageEntity messageEntity = new MessageEntity(MessageEntity.Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        entityManager.persist(messageEntity);
        MessageEntity foundMessageEntity = messageRepository.findById(messageEntity.getId()).orElseThrow(NoSuchElementException::new);
        messageRepository.delete(foundMessageEntity);
        assertThat(messageRepository.findById(messageEntity.getId()).isEmpty());
    }



}

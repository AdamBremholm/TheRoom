package iths.theroom.repository;


import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import org.junit.Before;
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

    MessageEntity message1;
    UserEntity userEntity1;
    RoomEntity roomEntity1;



    @Before
    public void setup() {
        message1 = new MessageEntity();
        userEntity1 = new UserEntity();
        roomEntity1 = new RoomEntity();
        userEntity1.setEmail("test@gmail.com");
        userEntity1.setUserName("test");
        message1.setSender(userEntity1);
        roomEntity1.setRoomName("one");
        message1.setRoomEntity(roomEntity1);

    }

    @Test
    public void saveRead() {
        entityManager.persist(message1);
        assertThat(messageRepository.findById(message1.getId()).orElseThrow(NoSuchElementException::new)).isEqualTo(message1);
    }
    @Test
    public void getByUuid() {
        entityManager.persist(message1);
        assertThat(messageRepository.findByUuid(message1.getUuid()).orElseThrow(NoSuchElementException::new)).isEqualTo(message1);
    }

    @Test
    public void getRoom() {
        entityManager.persist(message1);
        assertThat(messageRepository.findById(message1.getId()).orElseThrow(NoSuchElementException::new).getRoomEntity().getRoomName()).isEqualTo("one");
    }

    @Test
    public void getSender() {
        userEntity1.setUserName("sven");
        message1.setSender(userEntity1);
        entityManager.persist(message1);
        assertThat(messageRepository.findById(message1.getId()).orElseThrow(NoSuchElementException::new).getSender().getUserName()).isEqualTo("sven");
    }


    @Test
    public void updateRoom() {
        entityManager.persist(message1);
        message1.setRoomEntity(new RoomEntity("two"));
        entityManager.merge(message1);
        assertThat(messageRepository.findById(message1.getId()).orElseThrow(NoSuchElementException::new).getRoomEntity().getRoomName()).isEqualTo("two");
    }

    @Test
    public void updateSender() {
        entityManager.persist(message1);
        userEntity1.setUserName("johan");
        message1.setSender(userEntity1);
        entityManager.merge(message1);
        assertThat(messageRepository.findById(message1.getId()).orElseThrow(NoSuchElementException::new).getSender().getUserName()).isEqualTo("johan");
    }


    @Test
    public void getUserEntity() {
        userEntity1.setUserName("sven");
        message1.setSender(userEntity1);
        entityManager.persist(message1);
        assertThat(messageRepository.findById(message1.getId()).orElseThrow(NoSuchElementException::new).getSender().getUserName()).isEqualTo("sven");
    }


    @Test
    public void saveWithNewInfoUpdates() {
        entityManager.persist(message1);
        MessageEntity foundMessage = messageRepository.findById(message1.getId()).orElseThrow(NoSuchElementException::new);
        foundMessage.setContent("bye");
        messageRepository.save(message1);
        assertThat(messageRepository.findById(message1.getId()).orElseThrow(NoSuchElementException::new).getContent()).isEqualTo("bye");
    }

    @Test
    public void remove() {
        entityManager.persist(message1);
        MessageEntity foundMessage = messageRepository.findById(message1.getId()).orElseThrow(NoSuchElementException::new);
        messageRepository.delete(foundMessage);
        assertThat(messageRepository.findById(message1.getId()).isEmpty());
    }



}

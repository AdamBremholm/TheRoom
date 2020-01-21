

package iths.theroom.repository;

import iths.theroom.entity.AvatarEntity;
import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.UserEntity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    UserEntity userEntity;
    MessageEntity messageEntity;
    Set<MessageEntity> messageEntities = new HashSet<>();


    @Before
    public void setUp() {

        messageEntity = new MessageEntity();
        messageEntities.add(messageEntity);

        userEntity = new UserEntity();
        userEntity.setUserName("sven");
        userEntity.setFirstName("sven");
        userEntity.setLastName("svensson");
        userEntity.setEmail("sven@gmail.com");
        userEntity.setPassword("sve123");
        userEntity.setPasswordConfirm("sve123");


    }

    @Test
    public void saveRead() {
        entityManager.persist(userEntity);
        assertThat(userRepository.findById(userEntity.getId()).orElseThrow(NoSuchElementException::new)).isEqualTo(userEntity);
    }
    @Test
    public void getByUuid() {
        entityManager.persist(userEntity);
        assertThat(userRepository.findByUserName(userEntity.getUserName()).orElseThrow(NoSuchElementException::new).getUserName()).isEqualTo(userEntity.getUserName());
    }


    @Test
    public void getMessages() {
        entityManager.persist(userEntity);
        assertThat(userRepository.findById(userEntity.getId()).orElseThrow(NoSuchElementException::new).getMessages().isEmpty());
    }


    @Test
    public void updateMessages() {
        messageEntities.add(messageEntity);
        entityManager.persist(userEntity);
        userEntity.setMessages(messageEntities);
        entityManager.merge(userEntity);
        assertThat(userRepository.findById(userEntity.getId()).orElseThrow(NoSuchElementException::new).getMessages().contains(messageEntity));
    }



    @Test
    public void saveWithNewInfoUpdates() {

        entityManager.persist(userEntity);
        UserEntity foundUser = userRepository.findById(userEntity.getId()).orElseThrow(NoSuchElementException::new);
        userEntity.setFirstName("johan");
        userRepository.save(foundUser);
        assertThat(userRepository.findById(userEntity.getId()).orElseThrow(NoSuchElementException::new).getFirstName()).isEqualTo("johan");
    }

    @Test
    public void remove() {
        entityManager.persist(userEntity);
        UserEntity foundUser = userRepository.findById(userEntity.getId()).orElseThrow(NoSuchElementException::new);
        userRepository.delete(userEntity);
        assertThat(userRepository.findById(userEntity.getId()).isEmpty());
    }



}

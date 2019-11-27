package iths.theroom.service;

import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoleEntity;
import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.repository.MessageRepository;
import iths.theroom.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
public class UserServiceImplIntegrationTest {


    @TestConfiguration
    static class UserServiceImplIntegrationTestContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    RoleEntity roleEntity;
    UserEntity userEntity;
    MessageEntity messageEntity;
    Set<MessageEntity> messageEntities = new HashSet<>();
    Set<RoleEntity> roleEntities = new HashSet<>();


    @Before
    public void setUp() {

        messageEntity = new MessageEntity(MessageEntity.Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        messageEntities.add(messageEntity);
        roleEntity = new RoleEntity(RoleEntity.Role.USER);
        roleEntities.add(roleEntity);
        userEntity = new UserEntity("sven", "sve123", "sven@gmail.com"
                , roleEntities, "sve123", "sven", "svensson", new HashSet<>(), null);


        Mockito.when(userRepository.findByUserName(userEntity.getUserName()))
                .thenReturn(java.util.Optional.of(userEntity));
    }


    @Test
    public void whenValidUserName_thenMessageModelShouldBeReturned() {
        String userName = "sven";
        UserEntity found = userService.getByUserName("sven");

        assertThat(found.getUserName())
                .isEqualTo(userName);
    }


}
package iths.theroom.service;

import iths.theroom.entity.AvatarEntity;
import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.enums.Type;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.ConflictException;
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

    UserEntity userEntity1;
    UserEntity userEntity2;
    UserEntity userEntity3;
    MessageEntity messageEntity;
    Set<MessageEntity> messageEntities = new HashSet<>();


    @Before
    public void setUp() {

        messageEntity = new MessageEntity();
        messageEntities.add(messageEntity);
        userEntity1 = new UserEntity("sven", "sve123", "sven@gmail.com"
                , "sve123", "sven", "svensson", new HashSet<>(), null, new AvatarEntity());

        userEntity2 = new UserEntity("johan", "sve123", "johan@gmail.com"
                , "sve124", "sven", "svensson", new HashSet<>(), null, new AvatarEntity());


        userEntity3 = new UserEntity("johan", "sve123", "johan@gmail.com"
                , "sve123", "sven", "svensson", new HashSet<>(), null, new AvatarEntity());
        Mockito.when(userRepository.findByUserName(userEntity1.getUserName()))
                .thenReturn(java.util.Optional.of(userEntity1));

        Mockito.when(userRepository.findByEmail(userEntity2.getEmail()))
                .thenReturn(java.util.Optional.of(userEntity2));
    }



    @Test(expected = BadRequestException.class)
    public void passwordValidationThrowsBadRequestWhenNotMatchingWithConfirm() throws ConflictException, BadRequestException {
       userService.save(userEntity2);
    }

    @Test(expected = ConflictException.class)
    public void conflictExceptionIsThrownOnDuplicateUserName() throws ConflictException, BadRequestException {
        userService.save(userEntity1);
    }

    @Test(expected = ConflictException.class)
    public void conflictExceptionIsThrownOnDuplicateEmail() throws ConflictException, BadRequestException {
        userService.save(userEntity3);
    }






}

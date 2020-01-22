package iths.theroom.service;

import com.sun.security.auth.UserPrincipal;
import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.ConflictException;
import iths.theroom.exception.NotFoundException;
import iths.theroom.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

        @Bean
        PasswordEncoder getEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Mock
    HttpServletRequest httpServletRequest;



    UserEntity userEntity1;
    UserEntity userEntity2;
    UserEntity userEntity3;
    MessageEntity messageEntity;
    Set<MessageEntity> messageEntities;
    UserPrincipal userPrincipal;


    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        userPrincipal = new UserPrincipal("sven");
        messageEntities = new HashSet<>();
        messageEntity = new MessageEntity();
        messageEntities.add(messageEntity);

        userEntity1 = new UserEntity();
        userEntity1.setUserName("sven");
        userEntity1.setFirstName("sven");
        userEntity1.setLastName("svensson");
        userEntity1.setEmail("sven@gmail.com");
        userEntity1.setPassword("sve123");
        userEntity1.setPasswordConfirm("sve123");

        userEntity2 = new UserEntity();
        userEntity2.setUserName("johan");
        userEntity2.setPassword("sve123");
        userEntity2.setPasswordConfirm("sve124");
        userEntity2.setFirstName("sven");
        userEntity2.setLastName("svensson");
        userEntity2.setEmail("johan@gmail.com");


        userEntity3 = new UserEntity();
        userEntity3.setUserName("johan");
        userEntity3.setPassword("sve123");
        userEntity3.setPasswordConfirm("sve123");
        userEntity3.setFirstName("sven");
        userEntity3.setLastName("svensson");
        userEntity3.setEmail("johan@gmail.com");
        userEntity3.setRoles("ADMIN, USER");

    }


    @Test(expected = BadRequestException.class)
    public void passwordValidationThrowsBadRequestWhenNotMatchingWithConfirm() throws ConflictException, BadRequestException {
       userService.save(userEntity2);
    }

    @Test(expected = ConflictException.class)
    public void conflictExceptionIsThrownOnDuplicateUserName() throws ConflictException, BadRequestException {
        Mockito.when(userRepository.findByUserName(userEntity1.getUserName()))
                .thenReturn(java.util.Optional.of(userEntity1));
        userService.save(userEntity1);
    }

    @Test(expected = ConflictException.class)
    public void conflictExceptionIsThrownOnDuplicateEmail() throws ConflictException, BadRequestException {
        Mockito.when(userRepository.findByEmail(userEntity2.getEmail()))
                .thenReturn(java.util.Optional.of(userEntity2));
        userService.save(userEntity3);
    }

    @Test(expected = NotFoundException.class)
    public void getUserById_ThrowsExceptionWhenNotFound() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        userService.getUserById(123L);
    }

    @Test
    public void getUserById_NormalOperation() {
        Mockito.when(userRepository.findById(123L)).thenReturn(Optional.of(userEntity1));
        assertThat(userService.getUserById(123L)).isEqualTo(userEntity1);
    }

    @Test
    public void save_NormalOperation() {
        Mockito.when(userRepository.save(userEntity1)).thenReturn(userEntity1);
        assertThat(userService.save(userEntity1)).isEqualTo(userEntity1);
    }

    @Test(expected = NotFoundException.class)
    public void getByUserName_ThrowsExceptionWhenNotFound() {
        Mockito.when(userRepository.findByUserName(Mockito.any())).thenReturn(Optional.empty());
        userService.getByUserName("nonexistent");
    }

    @Test
    public void getAll() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(userEntity1));
        assertThat(userService.getAll().size()).isEqualTo(1);
    }

    @Test(expected = NotFoundException.class)
    public void loadUserByUsername_NotFoundThrowsException() {
        Mockito.when(userRepository.findByUserName(Mockito.any())).thenReturn(Optional.empty());
        userService.loadUserByUsername("nonexistent");
    }

    @Test
    public void loadUserByUsername_NormalOperation() {
        Mockito.when(userRepository.findByUserName(Mockito.any())).thenReturn(Optional.of(userEntity1));
        assertThat(userService.loadUserByUsername("sven")).isInstanceOf(UserDetails.class);
    }

    @Test
    public void isUserWhoItClaimsToBe() {
        Mockito.when(httpServletRequest.getUserPrincipal()).thenReturn(userPrincipal);
        assertThat(userService.isUserWhoItClaimsToBe("sven", httpServletRequest)).isTrue();
        assertThat(userService.isUserWhoItClaimsToBe("ronny", httpServletRequest)).isFalse();
    }


    @Test(expected = ConflictException.class)
    public void updateUser_ExceptionThrowsException() {
        Mockito.when(userRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        userService.updateUser(userEntity1);
    }


    @Test
    public void updateUser_NormalOperation() {
        Mockito.when(userRepository.findByUserName(Mockito.any())).thenReturn(Optional.of(userEntity1));
        assertThat(userService.updateUser(userEntity1).equals(userEntity1));

    }






}

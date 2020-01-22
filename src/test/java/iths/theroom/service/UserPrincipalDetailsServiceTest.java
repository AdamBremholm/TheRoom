package iths.theroom.service;

import iths.theroom.entity.UserEntity;
import iths.theroom.exception.NotFoundException;
import iths.theroom.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserPrincipalDetailsServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserPrincipalDetailsService userPrincipalDetailsService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NotFoundException.class)
    public void loadByUserName_DoesntGeneratePrincipalsForUnknownUser() {
        when(userRepository.findByUserName(any())).thenReturn(Optional.empty());
        userPrincipalDetailsService.loadUserByUsername("sven");
    }

    @Test
    public void loadByUserName_DoGeneratePrincipalsForUser() {
        when(userRepository.findByUserName(any())).thenReturn(Optional.of(new UserEntity("sven")));
        assertTrue(userPrincipalDetailsService.loadUserByUsername("sven").getUsername().equals("sven"));
    }
}

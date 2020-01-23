package iths.theroom.service;

import com.sun.security.auth.UserPrincipal;
import iths.theroom.entity.ProfileEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.exception.*;
import iths.theroom.pojos.ProfileForm;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.UserDetails;
import javax.servlet.http.HttpSession;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProfileServiceImplTest {

    MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

    @Mock
    UserServiceImpl userService;

    @Mock
    UserPrincipalDetailsService userPrincipalDetailsService;

    @Mock
    UserDetails userDetail;

    @InjectMocks
    ProfileServiceImpl profileService;

    UserPrincipal userPrincipal;


    @Before
    public void setUp() {
        HttpSession session = new MockHttpSession(null, "test-session-id");
        MockitoAnnotations.initMocks(this);
        userPrincipal = new UserPrincipal("sven");
        httpServletRequest.setSession(session);
        httpServletRequest.setRemoteAddr("1.2.3.4");
    }


    @Test(expected = UnauthorizedException.class)
    public void saveProfilAsTheWrongUser_ThrowsUnauthorizedException() {
        when(userDetail.getUsername()).thenReturn("erik");
        when(userPrincipalDetailsService.loadUserByUsername(any())).thenReturn(userDetail);
        when(userService.getByUserName(any())).thenReturn(new UserEntity("erik"));
        ProfileForm form = new ProfileForm();
        form.setUsername("erik");
        form.setAboutMe("");
        form.setAge(19);
        form.setCountry("");
        form.setGender("");
        form.setStarSign("");
        profileService.save(form, httpServletRequest);
    }

    @Test
    public void saveProfilAsTheCorrectUser_PassesAsNormal() {
        when(userDetail.getUsername()).thenReturn("erik");
        when(userPrincipalDetailsService.loadUserByUsername(any())).thenReturn(userDetail);
        when(userService.getByUserName(any())).thenReturn(new UserEntity("erik"));
        when(userService.isUserWhoItClaimsToBe(any(), any())).thenReturn(true);
        ProfileForm form = new ProfileForm();
        form.setUsername("erik");
        form.setAboutMe("");
        form.setAge(19);
        form.setCountry("sweden");
        form.setGender("");
        form.setStarSign("");
        ProfileEntity profile = new ProfileEntity();
        profile.setCountry(form.getCountry());
        UserEntity user = new UserEntity(form.getUsername());
        user.setProfile(profile);
        when(userService.updateUser(any())).thenReturn(user);
        assertTrue(profileService.save(form, httpServletRequest).getCountry().equals("sweden"));
    }

    @Test
    public void getProfileAsTheCorrectUser_PassesAsNormal() {
        UserEntity user = new UserEntity("bertil");
        ProfileEntity profile = new ProfileEntity();
        profile.setCountry("sweden");
        user.setProfile(profile);
        when(userService.getByUserName(any())).thenReturn(user);
        assertTrue(profileService.get(user.getUserName()).getCountry().equals(profile.getCountry()));
    }
}

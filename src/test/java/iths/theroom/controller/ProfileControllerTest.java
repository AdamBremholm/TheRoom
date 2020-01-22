package iths.theroom.controller;

import iths.theroom.entity.ProfileEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.exception.UnauthorizedException;
import iths.theroom.factory.ProfileFactory;
import iths.theroom.model.ProfileModel;
import iths.theroom.service.ProfileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=password",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
public class ProfileControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private ProfileService profileService;

    private MockMvc mvc;

    UserEntity user;
    ProfileEntity profile;
    ProfileModel profileModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        user = new UserEntity();
        user.setPassword("password");
        user.setPasswordConfirm("password");
        user.setUserName("erik");
        user.setEmail("erik@erik.erik");
        profile = new ProfileEntity();
        profile.setCountry("testcountry");
        user.setProfile(profile);
        profile.setGender("aa");
        profileModel = ProfileFactory.toModel(profile);
    }


    @WithMockUser(username="spring")
    @Test
    public void getProfileMustHaveAUserNameElseRejectRequest() throws Exception {
        mvc.perform(get("/api/profile/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @WithMockUser(username="spring")
    @Test
    public void getProfileReturnsOk() throws Exception {
        Mockito.when(profileService.get("name")).thenReturn(profileModel);
        mvc.perform(get("/api/profile/name")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.country", is(profile.getCountry())))
                .andExpect(status().isOk());
    }

    @WithMockUser(username="spring")
    @Test
    public void postProfileMustMatchUserPrincipalElseReturnUnauth() throws Exception {
        Mockito.when(profileService.save(any(), any())).thenThrow(new UnauthorizedException("Invalid credentials"));
        mvc.perform(post("/api/profile")
                .content("{\"password\": \"password\", \"username\": \"name\", \"gender\": \"female\", \"country\": \"sweden\", \"age\": 19, \"aboutMe\": \"i like turtles\", \"starSign\": \"lion\"}")
                .characterEncoding("UTF8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username="spring")
    @Test
    public void postProfileWorksAsExpected() throws Exception {
        Mockito.when(profileService.save(any(), any())).thenReturn(profileModel);
        mvc.perform(post("/api/profile")
                .content("{\"password\": \"password\", \"username\": \"name\", \"gender\": \"female\", \"country\": \"sweden\", \"age\": 19, \"aboutMe\": \"i like turtles\", \"starSign\": \"lion\"}")
                .characterEncoding("UTF8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.country", is(profile.getCountry())))
                .andExpect(status().isOk());
    }

    @WithMockUser(username="spring")
    @Test
    public void postProfileBadJsonEqualsBadRequest() throws Exception {
        Mockito.when(profileService.save(any(), any())).thenReturn(profileModel);
        mvc.perform(post("/api/profile")
                .content("not very good json format")
                .characterEncoding("UTF8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

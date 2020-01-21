package iths.theroom.controller;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import iths.theroom.entity.AvatarEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.factory.UserFactory;
import iths.theroom.model.JwtResponse;
import iths.theroom.model.MessageModel;
import iths.theroom.model.UserModel;
import iths.theroom.security.UserPrincipal;
import iths.theroom.service.UserPrincipalDetailsService;
import iths.theroom.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static reactor.core.publisher.Mono.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=password",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
public class UserControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserService userService;

    @MockBean
    private UserPrincipalDetailsService userPrincipalDetailsService;

    @Autowired
    ObjectMapper objectMapper;

    private UserModel userModel;
    private UserEntity userEntity;
    private List<UserEntity> allUsers;
    private String encodedPassword;

    private MockMvc mvc;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        userEntity = new UserEntity();
        userEntity.setUserName("sven23");
        userEntity.setEmail("sven@gmail.com");
        userEntity.setPassword("123abc");
        userEntity.setPasswordConfirm("123abc");
        userEntity.setRoles("");
        userModel = UserFactory.toModel(userEntity);
        allUsers = Collections.singletonList(userEntity);
        encodedPassword = new BCryptPasswordEncoder().encode("123abc");
    }

    @WithMockUser(username = "springuser")
    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {


        given(userService.getAll()).willReturn(allUsers);

        mvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userName", is(userModel.getUserName())));
    }


    @WithMockUser(username = "sven23")
    @Test
    public void performAuthentication() throws Exception {

        userEntity.setPassword(encodedPassword);
        given(userPrincipalDetailsService.loadUserByUsername(any())).willReturn(new UserPrincipal(userEntity));

        ResultActions resultActions = mvc.perform(post("/login")
                .content("{\n" +
                        "\t\"username\": \"sven23\",\n" +
                        "\t\"password\": \"123abc\"\n" +
                        "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        JwtResponse jwtResponse = objectMapper.readValue(contentAsString, JwtResponse.class);
        System.out.println(jwtResponse.getToken());

        given(userService.getByUserName(any())).willReturn(userEntity);

        mvc.perform(get("/api/users")
                .header("Authorization", "Bearer "+ jwtResponse.getToken() )
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @WithMockUser(username = "sven23")
    @Test(expected = JWTDecodeException.class)
    public void performAuthentication_WithNonValidToken_ThrowsException() throws Exception {

        userEntity.setPassword(encodedPassword);
        given(userPrincipalDetailsService.loadUserByUsername(any())).willReturn(new UserPrincipal(userEntity));

        ResultActions resultActions = mvc.perform(post("/login")
                .content("{\n" +
                        "\t\"username\": \"sven23\",\n" +
                        "\t\"password\": \"123abc\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        JwtResponse jwtResponse = objectMapper.readValue(contentAsString, JwtResponse.class);
        System.out.println(jwtResponse.getToken());

        userEntity.setRoles("USER");
        given(userService.getByUserName(any())).willReturn(userEntity);

        mvc.perform(get("/api/users")
                .header("Authorization", "Bearer "+ "eyJ0eXAiOiJKVxxxOiJIUzUxMiJ9.eyJzdWIiOiJzdmVuMjMiLCJleHAiOjE1ODA0OTk5MTh9.ML2Tj1yEVv-a7vQA4xxxxl0CRtSp6SwdANRZxs2bdTnitnczei0BHCYiX937Wc9ZP9UV_7rs6ytDVsLQ" )
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


    @Test
    public void AccessDenied_whenNotAuthenticated() throws Exception {

        mvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(unauthenticated());
    }


    @Test
    public void addUserWorksWithoutAuth() throws Exception {

        given(userService.save(any())).willReturn(userEntity);

        mvc.perform(post("/api/users")
                .content(objectMapper.writeValueAsString(userEntity))
                .characterEncoding("UTF8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @WithMockUser(username="spring", roles="ADMIN")
    @Test
    public void addUserBadSyntax_ReturnsBadRequest() throws Exception {


        given(userService.getAll()).willReturn(allUsers);

        mvc.perform(post("/api/users")
                .content("im not valid json")
                .characterEncoding("UTF8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}

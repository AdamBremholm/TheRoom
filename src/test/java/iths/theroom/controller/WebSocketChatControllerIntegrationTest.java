package iths.theroom.controller;

import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.model.MessageModel;
import iths.theroom.pojos.MessageForm;
import iths.theroom.repository.RoomRepository;
import iths.theroom.repository.UserRepository;
import iths.theroom.service.MessageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=password",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
public class WebSocketChatControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebSocketChatController webSocketChatController;

    @Mock
    Authentication authentication;

    @Before
    public void setup(){

        MockitoAnnotations.initMocks(this);

        UserEntity testUser1 = new UserEntity(
                "JohnDoe", "password", "john@email.com", "password",
                "John", "Doe", null, null);

        UserEntity testUser2 = new UserEntity(
                "AdamSmith", "password", "adam@email.com", "password",
                "Adam", "Smith", null, null);

        userRepository.saveAndFlush(testUser1);
        userRepository.saveAndFlush(testUser2);

        RoomEntity testRoom1 = new RoomEntity("TestRoom1", "#FFFFFF");
        RoomEntity testRoom2 = new RoomEntity("TestRoom2", "#000000");

        roomRepository.saveAndFlush(testRoom1);
        roomRepository.saveAndFlush(testRoom2);

    }

    @Test
    public void test() {

        Mockito.when(authentication.getName()).thenReturn("JohnDoe");

        MessageForm payload = new MessageForm();
        payload.setRoomBackgroundColor("#313131");
        payload.setRoomName("TestRoom1");

        ResponseEntity response = webSocketChatController.changeBackground("TestRoom1", payload, authentication);

        System.out.println(response.getBody());


    }
}

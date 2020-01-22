package iths.theroom.controller;

import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.pojos.MessageForm;
import iths.theroom.repository.RoomRepository;
import iths.theroom.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketChatControllerIntegrationTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebSocketChatController webSocketChatController;

    @Mock
    Authentication authentication;

    UserEntity testUser1;
    UserEntity testUser2;
    RoomEntity testRoom1;
    RoomEntity testRoom2;

    MessageForm messageForm1;

    @Before
    public void setup(){

        testUser1 = new UserEntity(
                "JohnDoe", "password", "john@email.com", "password",
                "John", "Doe", null, null);

        testUser2 = new UserEntity(
                "AdamSmith", "password", "adam@email.com", "password",
                "Adam", "Smith", null, null);

        userRepository.delete(testUser1);
        userRepository.delete(testUser2);
        userRepository.saveAndFlush(testUser1);
        userRepository.saveAndFlush(testUser2);

        testRoom1 = new RoomEntity("TestRoom1", "#FFFFFF");
        testRoom2 = new RoomEntity("TestRoom2", "#000000");

        roomRepository.delete(testRoom1);
        roomRepository.delete(testRoom2);
        roomRepository.saveAndFlush(testRoom1);
        roomRepository.saveAndFlush(testRoom2);

        messageForm1 = new MessageForm();
        messageForm1.setRoomName(testRoom1.getRoomName());
        messageForm1.setRoomBackgroundColor(testRoom1.getBackgroundColor());
        messageForm1.setSender(testUser1.getUserName());

    }

    @After
    public void tearDown(){
        userRepository.delete(testUser1);
        userRepository.delete(testUser2);
        roomRepository.delete(testRoom1);
        roomRepository.delete(testRoom2);
    }


    @Test
    public void changeBackground_returnCorrectString(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        String expected = "#313131";
        messageForm1.setRoomBackgroundColor(expected);
        ResponseEntity response = webSocketChatController.changeBackground(testRoom1.getRoomName(), messageForm1, authentication);

        String actual = (String) response.getBody();

        assertEquals(expected, actual);
    }

    @Test
    public void changeBackground_ifUserBannedReturnStatus401Unauthorized(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        Set<RoomEntity> bannedFromRooms = new HashSet<>();
        bannedFromRooms.add(testRoom1);
        testUser1.setExcludedRooms(bannedFromRooms);
        userRepository.saveAndFlush(testUser1);

        ResponseEntity response = webSocketChatController.changeBackground(testRoom1.getRoomName(), messageForm1, authentication);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void changeBackGround_ifUserDoesntExistReturnStatus404NotFound(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        userRepository.delete(testUser1);

        ResponseEntity response = webSocketChatController.changeBackground(testRoom1.getRoomName(), messageForm1, authentication);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void changeBackground_ifRoomDoesntExistReturnStatus404NotFound(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        roomRepository.delete(testRoom1);

        ResponseEntity response = webSocketChatController.changeBackground(testRoom1.getRoomName(), messageForm1, authentication);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}

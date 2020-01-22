package iths.theroom.controller;

import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.enums.Type;
import iths.theroom.model.MessageModel;
import iths.theroom.pojos.MessageForm;
import iths.theroom.repository.MessageRepository;
import iths.theroom.repository.RoomRepository;
import iths.theroom.repository.UserRepository;
import iths.theroom.service.MessageService;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketChatControllerIntegrationTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private WebSocketChatController webSocketChatController;

    @Mock
    Authentication authentication;

    private RoomEntity testRoom1 = new RoomEntity("019250271-98327469-653490102-234234", "#FFFFFF");
    private MessageEntity testMessage1 = new MessageEntity();
    private UserEntity testUser1 = new UserEntity(
            "234754-91293023-0239409-2134236", "password", "8432412@5219385.com", "password",
            "John", "Doe", null, null);

    private final String invalidRoomName = "0982350928234-2930481";
    private final String invalidUserName = "02158029834-209314021934";

    private MessageForm messageForm1;

    @Before
    public void setup(){
        messageRepository.delete(testMessage1);
        userRepository.delete(testUser1);
        roomRepository.delete(testRoom1);


        messageForm1 = new MessageForm();
        messageForm1.setRoomName(testRoom1.getRoomName());
        messageForm1.setRoomBackgroundColor(testRoom1.getBackgroundColor());
        messageForm1.setSender(testUser1.getUserName());
        messageForm1.setRating(0);
        messageForm1.setType(Type.CHAT);
        messageForm1.setContent("Hello World!");

        testUser1 = userRepository.saveAndFlush(testUser1);
        testRoom1 = roomRepository.saveAndFlush(testRoom1);
        messageService.save(messageForm1);
        testMessage1 = messageRepository.findAllBySenderAndRoomEntityOrderByTimeDesc(testUser1, testRoom1).get(0);

        testUser1 = userRepository.findUserByNameWithQuery(testUser1.getUserName());
        testRoom1 = roomRepository.findRoomByNameWithQuery(testRoom1.getRoomName());

    }

    @After
    public void tearDown(){
        List<MessageEntity> messages = messageRepository.findAllBySenderAndRoomEntityOrderByTimeDesc(testUser1, testRoom1);
        messages.forEach(messageRepository::delete);
        roomRepository.delete(testRoom1);
        userRepository.delete(testUser1);
        Optional<RoomEntity> roomEntity = roomRepository.getOneByRoomName(invalidRoomName);
        roomEntity.ifPresent(roomRepository::delete);
    }

    private void banUser(RoomEntity room, UserEntity user){
        Set<RoomEntity> bannedFromRooms = new HashSet<>();
        bannedFromRooms.add(room);
        user.setExcludedRooms(bannedFromRooms);
        userRepository.saveAndFlush(user);
    }

    @Test
    public void sendMessage_returnCorrectModelAnd200OK(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        ResponseEntity response = webSocketChatController.sendMessage(messageForm1, authentication);
        assertNotNull(response.getBody());
        MessageModel model = (MessageModel) response.getBody();

        assertNotNull(model);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void sendMessage_ifUserBannedReturnStatus401Unauthorized(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        banUser(testRoom1, testUser1);

        ResponseEntity response = webSocketChatController.sendMessage(messageForm1, authentication);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void sendMessage_ifUserDoesntExistReturnStatus404NotFound(){
        Mockito.when(authentication.getName()).thenReturn(invalidUserName);

        ResponseEntity response = webSocketChatController.sendMessage(messageForm1, authentication);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void sendMessage_ifRoomDoesntExistReturnStatus404NotFound(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        messageForm1.setRoomName(invalidRoomName);

        ResponseEntity response = webSocketChatController.sendMessage(messageForm1, authentication);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void changeBackground_returnCorrectStringAnd200OK(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        String expected = "#313131";
        messageForm1.setRoomBackgroundColor(expected);
        ResponseEntity response = webSocketChatController.changeBackground(messageForm1, authentication);

        String actual = (String) response.getBody();

        assertEquals(expected, actual);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void changeBackground_ifUserBannedReturnStatus401Unauthorized(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        banUser(testRoom1, testUser1);

        ResponseEntity response = webSocketChatController.changeBackground(messageForm1, authentication);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void changeBackGround_ifUserDoesntExistReturnStatus404NotFound(){
        Mockito.when(authentication.getName()).thenReturn(invalidUserName);

        ResponseEntity response = webSocketChatController.changeBackground(messageForm1, authentication);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void changeBackground_ifRoomDoesntExistReturnStatus404NotFound(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        messageForm1.setRoomName(invalidRoomName);

        ResponseEntity response = webSocketChatController.changeBackground(messageForm1, authentication);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void increaseRating_returnCorrectMessageModelAnd200OK(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        ResponseEntity response = webSocketChatController.increaseRating(testMessage1.getUuid(), testRoom1.getRoomName(), authentication);

        MessageModel messageModel = (MessageModel) response.getBody();
        assertNotNull(messageModel);
        int ratingBefore = testMessage1.getMessageRatingEntity().getRating();
        int ratingAfter = messageModel.getRating();

        assertEquals(ratingAfter, ratingBefore+1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void increaseRating_ensureUserCanIncreaseMaxOnce(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        webSocketChatController.increaseRating(testMessage1.getUuid(), testRoom1.getRoomName(), authentication);
        ResponseEntity response = webSocketChatController.increaseRating(testMessage1.getUuid(), testRoom1.getRoomName(), authentication);

        MessageModel messageModel = (MessageModel) response.getBody();
        assertNotNull(messageModel);
        int ratingBefore = testMessage1.getMessageRatingEntity().getRating();
        int ratingAfter = messageModel.getRating();

        assertEquals(ratingAfter, ratingBefore+1);
    }

    @Test
    public void increaseRating_ifUserDecreasedEnsureUserCanUndoAndIncrease(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        ResponseEntity ratingDecrease = webSocketChatController.decreaseRating(testMessage1.getUuid(), testRoom1.getRoomName(), authentication);
        MessageModel afterDecreasedRating = (MessageModel) ratingDecrease.getBody();
        assertNotNull(afterDecreasedRating);
        int ratingBefore = afterDecreasedRating.getRating();

        webSocketChatController.increaseRating(testMessage1.getUuid(), testRoom1.getRoomName(), authentication);
        ResponseEntity response = webSocketChatController.increaseRating(testMessage1.getUuid(), testRoom1.getRoomName(), authentication);

        MessageModel messageModel = (MessageModel) response.getBody();
        assertNotNull(messageModel);

        int ratingAfter = messageModel.getRating();

        assertEquals(ratingAfter, ratingBefore+2);
    }

    @Test
    public void increaseRating_ifUserBannedReturnStatus401Unauthorized(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        banUser(testRoom1, testUser1);

        ResponseEntity response = webSocketChatController.increaseRating(testMessage1.getUuid(), testRoom1.getRoomName(), authentication);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void increaseRating_ifUserDoesntExistReturnStatus404NotFound(){
        Mockito.when(authentication.getName()).thenReturn(invalidUserName);

        ResponseEntity response = webSocketChatController.increaseRating(testMessage1.getUuid(), testRoom1.getRoomName(), authentication);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void increaseRating_ifRoomDoesntExistReturnStatus404NotFound(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        ResponseEntity response = webSocketChatController.increaseRating(testMessage1.getUuid(), invalidRoomName, authentication);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void increaseRating_ifMessageDoesntExistReturnStatus404NotFound(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        ResponseEntity response = webSocketChatController.increaseRating("invalid-uuid", testRoom1.getRoomName(), authentication);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void decreaseRating_returnCorrectMessageModelAnd200OK(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        ResponseEntity response = webSocketChatController.decreaseRating(testMessage1.getUuid(), testRoom1.getRoomName(), authentication);

        MessageModel messageModel = (MessageModel) response.getBody();
        assertNotNull(messageModel);
        int ratingBefore = testMessage1.getMessageRatingEntity().getRating();
        int ratingAfter = messageModel.getRating();

        assertEquals(ratingAfter, ratingBefore-1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void decreaseRating_ensureUserCanDecreaseMaxOnce(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        webSocketChatController.decreaseRating(testMessage1.getUuid(), testRoom1.getRoomName(), authentication);
        ResponseEntity response = webSocketChatController.decreaseRating(testMessage1.getUuid(), testRoom1.getRoomName(), authentication);

        MessageModel messageModel = (MessageModel) response.getBody();
        assertNotNull(messageModel);
        int ratingBefore = testMessage1.getMessageRatingEntity().getRating();
        int ratingAfter = messageModel.getRating();

        assertEquals(ratingAfter, ratingBefore-1);
    }

    @Test
    public void decreaseRating_ifUserIncreasedEnsureUserCanUndoAndDecrease(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        ResponseEntity ratingIncreased = webSocketChatController.increaseRating(testMessage1.getUuid(), testRoom1.getRoomName(), authentication);
        MessageModel afterIncreasedRating = (MessageModel) ratingIncreased.getBody();
        assertNotNull(afterIncreasedRating);
        int ratingBefore = afterIncreasedRating.getRating();

        webSocketChatController.decreaseRating(testMessage1.getUuid(), testRoom1.getRoomName(), authentication);
        ResponseEntity response = webSocketChatController.decreaseRating(testMessage1.getUuid(), testRoom1.getRoomName(), authentication);

        MessageModel messageModel = (MessageModel) response.getBody();
        assertNotNull(messageModel);

        int ratingAfter = messageModel.getRating();

        assertEquals(ratingAfter, ratingBefore-2);
    }

    @Test
    public void decreaseRating_ifUserBannedReturnStatus401Unauthorized(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        banUser(testRoom1, testUser1);

        ResponseEntity response = webSocketChatController.decreaseRating(testMessage1.getUuid(), testRoom1.getRoomName(), authentication);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void decreaseRating_ifUserDoesntExistReturnStatus404NotFound(){
        Mockito.when(authentication.getName()).thenReturn(invalidUserName);

        ResponseEntity response = webSocketChatController.decreaseRating(testMessage1.getUuid(), testRoom1.getRoomName(), authentication);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void decreaseRating_ifRoomDoesntExistReturnStatus404NotFound(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        ResponseEntity response = webSocketChatController.decreaseRating(testMessage1.getUuid(), invalidRoomName, authentication);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void decreaseRating_ifMessageDoesntExistReturnStatus404NotFound(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        ResponseEntity response = webSocketChatController.decreaseRating("invalid-uuid", testRoom1.getRoomName(), authentication);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getAllMessages_returnCorrectModelListAnd200OK(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        ResponseEntity response = webSocketChatController.getAllMessages(testRoom1.getRoomName(), authentication);
        assertNotNull(response.getBody());
        List messageList = (List) response.getBody();
        MessageModel message = (MessageModel) messageList.get(0);

        assertEquals(testMessage1.getUuid(), message.getUuid());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getAllMessages_ifRoomDoesntExistCreateNewRoomAndReturnEmptyList(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        ResponseEntity response = webSocketChatController.getAllMessages(invalidRoomName, authentication);
        assertNotNull(response.getBody());
        List messageList = (List) response.getBody();

        RoomEntity roomEntity = roomRepository.findRoomByNameWithQuery(invalidRoomName);
        assertNotNull(roomEntity);
        assertEquals(0, messageList.size());
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void getAllMessages_ifUserBannedReturnStatus401Unauthorized(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        banUser(testRoom1, testUser1);

        ResponseEntity response = webSocketChatController.getAllMessages(testRoom1.getRoomName(), authentication);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void getAllMessages_ifUserDoesntExistReturnStatus404NotFound(){
        Mockito.when(authentication.getName()).thenReturn(invalidUserName);

        ResponseEntity response = webSocketChatController.getAllMessages(testRoom1.getRoomName(), authentication);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void newUser_returnCorrectModelAnd200OK(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        messageForm1.setType(Type.newUser);

        ResponseEntity response = webSocketChatController.newUser(testRoom1.getRoomName(), messageForm1, authentication);
        assertNotNull(response.getBody());
        MessageModel model = (MessageModel) response.getBody();
        assertNotNull(model);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void newUser_ifUserBannedReturnStatus401Unauthorized(){
        Mockito.when(authentication.getName()).thenReturn(testUser1.getUserName());

        banUser(testRoom1, testUser1);
        messageForm1.setType(Type.newUser);

        ResponseEntity response = webSocketChatController.newUser(testRoom1.getRoomName(), messageForm1, authentication);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void newUser_ifUserDoesntExistReturnStatus404NotFound(){
        Mockito.when(authentication.getName()).thenReturn(invalidUserName);

        messageForm1.setType(Type.newUser);

        ResponseEntity response = webSocketChatController.newUser(testRoom1.getRoomName(), messageForm1, authentication);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }



}

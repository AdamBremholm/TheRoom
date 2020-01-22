package iths.theroom.service;

import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.MessageRatingEntity;
import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.NoSuchUserException;
import iths.theroom.exception.NotFoundException;
import iths.theroom.model.MessageModel;
import iths.theroom.pojos.MessageForm;
import iths.theroom.repository.MessageRepository;
import iths.theroom.repository.RoomRepository;
import iths.theroom.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;


@RunWith(SpringRunner.class)
public class MessageServiceImplIntegrationTest {

    @InjectMocks
    private MessageService messageService = new MessageServiceImpl();

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;


    UserEntity userEntity;
    RoomEntity roomEntity;
    MessageEntity message;
    MessageForm messageForm;
    MessageRatingEntity messageRatingEntity;
    Set<UserEntity> bannedUsers;

    @Before
    public void setUp() {
        message = new MessageEntity();
        messageForm = new MessageForm();

        String content = "hello";
        message.setUuid("123abc");
        message.setContent(content);

        userEntity = new UserEntity();
        userEntity.setUserName("sven");
        userEntity.setFirstName("sven");
        userEntity.setLastName("svensson");
        userEntity.setEmail("sven@gmail.com");
        userEntity.setPassword("sve123");
        userEntity.setPasswordConfirm("sve123");


        message.setSender(userEntity);
        roomEntity = new RoomEntity();
        roomEntity.setRoomName("room1");
        message.setRoomEntity(roomEntity);
        messageRatingEntity = new MessageRatingEntity();
        messageRatingEntity.setRating(0);
        message.setMessageRatingEntity(messageRatingEntity);

    }

    @Test
    public void constructor() {
        MessageServiceImpl messageService2 = new MessageServiceImpl(messageRepository, userRepository, roomRepository);
        assertThat(messageService2).isNotNull();
    }


    @Test
    public void whenValidUuid_thenMessageModelShouldBeReturned() {
        Mockito.when(messageRepository.findByUuid(message.getUuid()))
                .thenReturn(java.util.Optional.of(message));

        String content = "hello";
        MessageModel found = messageService.getMessageByUuid("123abc");

        assertThat(found.getContent())
                .isEqualTo(content);
    }


    @Test
    public void getAllMessages_returnsListOfAll() {  Mockito.when(messageRepository.findAll())
            .thenReturn(List.of(message));
       assertThat(messageService.getAllMessages().size()).isEqualTo(1);
    }

    @Test(expected = NoSuchUserException.class)
    public void save_NoUserFoundThrowsException() {
        Mockito.when(messageRepository.findByUuid(message.getUuid()))
                .thenReturn(java.util.Optional.of(message));

        Mockito.when(messageRepository.findAll())
                .thenReturn(List.of(message));

        Mockito.when(userRepository.findByUserName(any()))
                .thenReturn(Optional.empty());

        assertThat(messageService.save(messageForm)).isInstanceOf(MessageModel.class);
    }

    @Test(expected = BadRequestException.class)
    public void save_NoRoomFoundThrowsException() {
        Mockito.when(messageRepository.findByUuid(message.getUuid()))
                .thenReturn(java.util.Optional.of(message));

        Mockito.when(messageRepository.findAll())
                .thenReturn(List.of(message));

        Mockito.when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(userEntity));

        Mockito.when(roomRepository.getOneByRoomName(any()))
                .thenReturn(Optional.empty());

        assertThat(messageService.save(messageForm)).isInstanceOf(MessageModel.class);
    }

    @Test
    public void save_BanInUserNameBansUser() {
        Mockito.when(messageRepository.findByUuid(message.getUuid()))
                .thenReturn(java.util.Optional.of(message));

        Mockito.when(messageRepository.findAll())
                .thenReturn(List.of(message));

        userEntity.setUserName("ban");
        Mockito.when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(userEntity));

        Mockito.when(roomRepository.getOneByRoomName(any()))
                .thenReturn(Optional.of(roomEntity));

        Mockito.when(messageRepository.save(any()))
                .thenReturn(message);

        Mockito.when(roomRepository.save(any()))
                .thenReturn(null);

        Mockito.when(userRepository.save(any()))
                .thenReturn(null);

        messageService.save(messageForm);
        assertThat(roomEntity.getBannedUsers().contains(userEntity));

    }

    @Test
    public void save_NormalOperations() {
        Mockito.when(messageRepository.findByUuid(message.getUuid()))
                .thenReturn(java.util.Optional.of(message));

        Mockito.when(messageRepository.findAll())
                .thenReturn(List.of(message));

        Mockito.when(userRepository.findByUserName(any()))
                .thenReturn(Optional.of(userEntity));

        Mockito.when(roomRepository.getOneByRoomName(any()))
                .thenReturn(Optional.of(roomEntity));

        Mockito.when(roomRepository.save(any()))
                .thenReturn(null);

        Mockito.when(userRepository.save(any()))
                .thenReturn(null);

        Mockito.when(messageRepository.save(any()))
                .thenReturn(message);
     assertThat(messageService.save(messageForm)).isInstanceOf(MessageModel.class);
    }

    @Test
    public void remove() {
        messageService.remove("123abc");
        assertThat(1).isEqualTo(1);
    }

    @Test(expected = NotFoundException.class)
    public void getAllMessagesFromUser_emptyResultYieldsNotFoundException() {
        Mockito.when(messageRepository.findAll()).thenReturn(List.of());
        messageService.getAllMessagesFromUser("sven", "room1", "1");
    }

    @Test(expected = NotFoundException.class)
    public void getAllMessagesFromUser_countExceededBecauseOfWrongRoomNameYieldsNotFoundException() {
        Mockito.when(messageRepository.findAll()).thenReturn(List.of(message));
        messageService.getAllMessagesFromUser("sven", "wrongroomname", "1");
    }

    @Test(expected = NotFoundException.class)
    public void getAllMessagesFromUser_countIsMoreThanReturnedMessages() {
        Mockito.when(messageRepository.findAll()).thenReturn(List.of(message));
        messageService.getAllMessagesFromUser("sven", "room1", "3");
    }

    @Test(expected = BadRequestException.class)
    public void getAllMessagesFromUser_countIsNotNumberThrowsException() {
        Mockito.when(messageRepository.findAll()).thenReturn(List.of(message));
        Mockito.when(userRepository.findByUserName("sven")).thenReturn(Optional.ofNullable(userEntity));
        messageService.getAllMessagesFromUser("sven", "room1", "z");
    }

    @Test
    public void getAllMessagesFromUser_NormalOperationCountLowerThanMessages() {
        Mockito.when(userRepository.findByUserName(any())).thenReturn(Optional.ofNullable(userEntity));
        Mockito.when(roomRepository.getOneByRoomName(any())).thenReturn(Optional.ofNullable(roomEntity));
        Mockito.when(messageRepository.findAllBySenderOrderByTimeDesc(any(UserEntity.class))).thenReturn(List.of(message, message, message));
        Mockito.when(messageRepository.findAllBySenderAndRoomEntityOrderByTimeDesc(any(UserEntity.class), any(RoomEntity.class))).thenReturn(List.of(message, message, message));
        Assert.assertTrue(messageService.getAllMessagesFromUser("sven", "room1", "1").size() == 1);
    }

    @Test
    public void getAllMessagesFromUser_NormalOperation() {
        Mockito.when(userRepository.findByUserName(any())).thenReturn(Optional.ofNullable(userEntity));
        Mockito.when(roomRepository.getOneByRoomName(any())).thenReturn(Optional.ofNullable(roomEntity));
        Mockito.when(messageRepository.findAllBySenderOrderByTimeDesc(any(UserEntity.class))).thenReturn(List.of(message));
        Mockito.when(messageRepository.findAllBySenderAndRoomEntityOrderByTimeDesc(any(UserEntity.class), any(RoomEntity.class))).thenReturn(List.of(message));
        assertThat(messageService.getAllMessagesFromUser("sven", "room1", "1").get(0)).isInstanceOf(MessageModel.class);
    }

    @Test(expected = NotFoundException.class)
    public void decreaseMessageRating_messageNotFoundThrowsException() {
        Mockito.when(messageRepository.findByUuid(any())).thenReturn(Optional.empty());
        messageService.decreaseMessageRating("123", "johan");
    }

    @Test(expected = NoSuchUserException.class)
    public void decreaseMessageRating_userNotFoundThrowsException() {
        Mockito.when(messageRepository.findByUuid(any())).thenReturn(Optional.of(message));
        Mockito.when(userRepository.findByUserName(any())).thenReturn(Optional.empty());
        messageService.decreaseMessageRating("123", "johan");
    }

    @Test
    public void decreaseMessageRating_normalOperations() {
        Mockito.when(messageRepository.findByUuid(any())).thenReturn(Optional.of(message));
        Mockito.when(userRepository.findByUserName(any())).thenReturn(Optional.of(userEntity));
        assertThat(messageService.decreaseMessageRating("123abc", "sven").getRating()).isEqualTo(-1);
    }

    @Test(expected = NotFoundException.class)
    public void increaseMessageRating_messageNotFoundThrowsException() {
        Mockito.when(messageRepository.findByUuid(any())).thenReturn(Optional.empty());
        messageService.increaseMessageRating("123", "johan");
    }

    @Test(expected = NoSuchUserException.class)
    public void increaseMessageRating_userNotFoundThrowsException() {
        Mockito.when(messageRepository.findByUuid(any())).thenReturn(Optional.of(message));
        Mockito.when(userRepository.findByUserName(any())).thenReturn(Optional.empty());
        messageService.increaseMessageRating("123", "johan");
    }

    @Test
    public void increaseMessageRating_normalOperations() {
        Mockito.when(messageRepository.findByUuid(any())).thenReturn(Optional.of(message));
        Mockito.when(userRepository.findByUserName(any())).thenReturn(Optional.of(userEntity));
        assertThat(messageService.increaseMessageRating("123abc", "sven").getRating()).isEqualTo(1);
    }

    @Test
    public void findAllByRoomEntityOrderById_List() {
        roomEntity.addMessage(message);
        MessageEntity message1 = new MessageEntity();
        MessageEntity message2 = new MessageEntity();
        MessageEntity message3 = new MessageEntity();
        message1.setContent(message.getContent());
        message2.setContent("im in the middle");
        message3.setContent(message.getContent());
        Mockito.when(roomRepository.getOneByRoomName(any())).thenReturn(Optional.ofNullable(roomEntity));
        Mockito.when(messageRepository.findAllByRoomEntityOrderById(any())).thenReturn(List.of(message1, message2, message3));
        assertTrue(messageService.findAllByRoomEntityOrderById(roomEntity.getRoomName()).get(1).getContent().equals("im in the middle"));
    }
}

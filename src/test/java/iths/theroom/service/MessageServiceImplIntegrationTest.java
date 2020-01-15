package iths.theroom.service;

import iths.theroom.entity.AvatarEntity;
import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.factory.MessageFactory;
import iths.theroom.model.MessageModel;
import iths.theroom.pojos.MessageForm;
import iths.theroom.repository.MessageRepository;
import iths.theroom.repository.RoomRepository;
import iths.theroom.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


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

    @Before
    public void setUp() {
        message = new MessageEntity();
        messageForm = new MessageForm();

        String content = "hello";
        message.setUuid("123abc");
        message.setContent(content);

        userEntity = new UserEntity("sven", "sve123", "sven@gmail.com"
                , "sve123", "sven", "svensson", new HashSet<>(), new AvatarEntity(),"USER", "");
        roomEntity = new RoomEntity();

        Mockito.when(messageRepository.findByUuid(message.getUuid()))
                .thenReturn(java.util.Optional.of(message));

        Mockito.when(messageRepository.findAll())
                .thenReturn(List.of(message));

        Mockito.when(userRepository.findByUserName(Mockito.any()))
                .thenReturn(Optional.of(userEntity));

        Mockito.when(roomRepository.getOneByRoomName(Mockito.any()))
                .thenReturn(Optional.of(roomEntity));

        Mockito.when(roomRepository.save(Mockito.any()))
                .thenReturn(null);

        Mockito.when(userRepository.save(Mockito.any()))
                .thenReturn(null);

        Mockito.when(messageRepository.save(Mockito.any()))
                .thenReturn(message);



    }




    @Test
    public void whenValidUuid_thenMessageModelShouldBeReturned() {
        String content = "hello";
        MessageModel found = messageService.getMessageByUuid("123abc");

        assertThat(found.getContent())
                .isEqualTo(content);
    }


    @Test
    public void getAllMessages_returnsListOfAll() {
       assertThat(messageService.getAllMessages().size()).isEqualTo(1);
    }

    @Test
    public void save() {
     assertThat(messageService.save(messageForm)).isInstanceOf(MessageModel.class);
    }

    @Test
    public void remove() {
    }

    @Test
    public void getAllMessagesFromUser() {
    }

    @Test
    public void decreaseMessageRating() {
    }

    @Test
    public void increaseMessageRating() {
    }
}

package iths.theroom.service;

import iths.theroom.entity.MessageEntity;
import iths.theroom.factory.MessageFactory;
import iths.theroom.model.MessageModel;
import iths.theroom.repository.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
public class MessageServiceImplIntegrationTest {

    @InjectMocks
    private MessageService messageService = new MessageServiceImpl();

    @Mock
    private MessageRepository messageRepository;

    @Before
    public void setUp() {
        message = new MessageEntity();
        String content = "hello";
        message.setUuid("123abc");
        message.setContent(content);

        Mockito.when(messageRepository.findByUuid(message.getUuid()))
                .thenReturn(java.util.Optional.of(message));

        Mockito.when(messageRepository.findAll())
                .thenReturn(List.of(message));
    }

    MessageEntity message;


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

package iths.theroom.service;

import iths.theroom.entity.MessageEntity;
import iths.theroom.model.MessageModel;
import iths.theroom.repository.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
public class MessageServiceImplIntegrationTest {

    @InjectMocks
    private MessageService messageService = new MessageServiceImpl();

    @Mock
    private MessageRepository messageRepository;

    @Before
    public void setUp() {
        MessageEntity message = new MessageEntity();
        String content = "hello";
        message.setUuid("123abc");
        message.setContent(content);

        Mockito.when(messageRepository.findByUuid(message.getUuid()))
                .thenReturn(java.util.Optional.of(message));
    }


    @Test
    public void whenValidUuid_thenMessageModelShouldBeReturned() {
        String content = "hello";
        MessageModel found = messageService.getMessageByUuid("123abc");

        assertThat(found.getContent())
                .isEqualTo(content);
    }


}

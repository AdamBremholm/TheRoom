package iths.theroom.service;

import iths.theroom.dao.MessageRepository;
import iths.theroom.entity.Message;
import iths.theroom.entity.Room;
import iths.theroom.entity.UserEntity;
import iths.theroom.model.MessageModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
public class MessageServiceImplIntegrationTest {

    @TestConfiguration
    static class MessageServiceImplTestContextConfiguration {

        @Bean
        public MessageService messageService() {
            return new MessageServiceImpl();
        }
    }

    @Autowired
    private MessageService messageService;

    @MockBean
    private MessageRepository messageRepository;

    @Before
    public void setUp() {
        Message message = new Message(Message.Type.CHAT, "hello", new UserEntity("sven"), new Room("one"));
        message.setUuid("123abc");

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
package iths.theroom.service;

import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.repository.MessageRepository;
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
        MessageEntity message = new MessageEntity(MessageEntity.Type.CHAT, "hello", new UserEntity("sven"), new RoomEntity("one"));
        message.setUuid("123abc");

        Mockito.when(messageRepository.findByUuid(message.getUuid()))
                .thenReturn(java.util.Optional.of(message));
    }


    @Test
    public void whenValidUuid_thenMessageModelShouldBeReturned() {
        String content = "hello";
        MessageEntity found = messageService.getMessageByUuid("123abc");

        assertThat(found.getContent())
                .isEqualTo(content);
    }


}
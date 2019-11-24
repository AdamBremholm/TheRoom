package iths.theroom.controller;

import iths.theroom.entity.Message;
import iths.theroom.entity.Room;
import iths.theroom.entity.UserEntity;
import iths.theroom.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.Collections;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MessageController.class)
public class MessageControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MessageService service;


    @Test
    @WithMockUser(username="user")
    public void givenMessages_whenGetMessages_thenReturnJsonArray()
            throws Exception {

        Message message = new Message(Message.Type.CHAT, "hello", new UserEntity("sven"), new Room("one"));

        List<Message> allMessages = Collections.singletonList(message);

        given(service.getAllMessages()).willReturn(allMessages);

        mvc.perform(get("/api/messages")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].content", is(message.getContent())));
    }


    @Test
    @WithMockUser(username="user")
    public void addMessage()
            throws Exception {

        Message message = new Message(Message.Type.CHAT, "hello", new UserEntity("sven"), new Room("one"));
        List<Message> allMessages = Collections.singletonList(message);

        given(service.getAllMessages()).willReturn(allMessages);

        mvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].content", is(message.getContent())));
    }
}

package iths.theroom.controller;

import iths.theroom.model.MessageModel;
import iths.theroom.service.MessageService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MessageController.class)
@ActiveProfiles("test")
public class MessageControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MessageService service;

    private MessageModel message;


    @Before
    public void setup(){
        message = new MessageModel();
        message.setSender("sven");
        message.setContent("hello");
        message.setRoom("one");
    }


    @Test
    @WithMockUser(username="user")
    public void givenMessages_whenGetMessages_thenReturnJsonArray()
            throws Exception {

        List<MessageModel> allMessages = Collections.singletonList(message);

        given(service.getAllMessages()).willReturn(allMessages);

        mvc.perform(get("/api/messages")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].content", is(message.getContent())));
    }


    /*@Test
    //TODO: Needs to mock filters to check 403
    @WithMockUser(username="admin",roles={"ADMIN"})
    public void addMessage()
            throws Exception {

        List<MessageModel> allMessages = Collections.singletonList(message);

        given(service.getAllMessages()).willReturn(allMessages);

        mvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].content", is(message.getContent())));
    }*/
}

package iths.theroom.controller;

import iths.theroom.model.MessageModel;
import iths.theroom.service.MessageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class MessageControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private MessageService service;

    private MessageModel message;

    private MockMvc mvc;

    @Before
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        message = new MessageModel();
        message.setSender("sven");
        message.setContent("hello");
        message.setRoom("one");
    }

    @WithMockUser(username="spring", roles="ADMIN")
    @Test
    public void givenMessages_whenGetMessages_thenReturnJsonArray() throws Exception {

        List<MessageModel> allMessages = Collections.singletonList(message);

        given(service.getAllMessages()).willReturn(allMessages);

        mvc.perform(get("/api/messages")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].content", is(message.getContent())));
    }

    @Test
    public void fetchingMessagesAsUser_ReturnsForbiddenErrorCode() throws Exception {
        mvc.perform(get("/api/messages").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="spring", roles="ADMIN")
    @Test
    public void addMessageWorksAsExpected() throws Exception {

        List<MessageModel> allMessages = Collections.singletonList(message);

        given(service.getAllMessages()).willReturn(allMessages);

        mvc.perform(post("/api/messages")
                .content("{\"type\":\"CHAT\",\"content\":\"hello world\",\"roomName\":\"TheRoomb\",\"userName\":\"erik\"}")
                .characterEncoding("UTF8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username="spring", roles="ADMIN")
    @Test
    public void addMessageBadSyntax_ReturnsBadRequest() throws Exception {

        List<MessageModel> allMessages = Collections.singletonList(message);

        given(service.getAllMessages()).willReturn(allMessages);

        mvc.perform(post("/api/messages")
                .content("im not valid json")
                .characterEncoding("UTF8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addMessageAsUserReturnsForbiddenErrorCode() throws Exception {

        List<MessageModel> allMessages = Collections.singletonList(message);

        given(service.getAllMessages()).willReturn(allMessages);

        mvc.perform(post("/api/messages")
                .content("{\"type\":\"CHAT\",\"content\":\"hello world\",\"roomName\":\"TheRoomb\",\"userName\":\"erik\"}")
                .characterEncoding("UTF8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}

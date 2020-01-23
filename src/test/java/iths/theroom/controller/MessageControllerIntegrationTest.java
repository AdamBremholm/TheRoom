package iths.theroom.controller;

import iths.theroom.exception.NotFoundException;
import iths.theroom.model.MessageModel;
import iths.theroom.service.MessageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=password",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
public class MessageControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private MessageService service;

    private MessageModel message;

    private MockMvc mvc;

    List<MessageModel> allMessages;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        message = new MessageModel();
        message.setSender("sven");
        message.setContent("hello");
        message.setRoom("one");
        allMessages = Collections.singletonList(message);
    }

    @WithMockUser(username="spring", roles="ADMIN")
    @Test
    public void givenMessages_whenGetMessages_thenReturnJsonArray() throws Exception {


        given(service.getAllMessages()).willReturn(allMessages);

        mvc.perform(get("/api/messages")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].content", is(message.getContent())));
    }

    @WithMockUser(username = "spring", roles="ADMIN")
    @Test
    public void whenGetOneMessage_thenReturnJsonObject() throws Exception {

        given(service.getMessageByUuid(any())).willReturn(message);

        mvc.perform(get("/api/messages/abcdf-abcdef-abcdef-abcdef")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is(message.getContent())));
    }

    @WithMockUser(username = "spring", roles="ADMIN")
    @Test
    public void whenGetOneMessage_AndMessageNotFound_thenReturn_NotFoundException() throws Exception {

        given(service.getMessageByUuid(any())).willThrow(NotFoundException.class);

        mvc.perform(get("/api/messages/abcdf-abcdef-abcdef-abcdef")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "user", roles="ADMIN")
    @Test
    public void whenSearch_ReturnJsonArray() throws Exception {

        given(service.getAllMessagesFromUser(any(), any(), any())).willReturn(allMessages);

        mvc.perform(get("/api/messages/search?username=ronny&roomname=hej&count=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "user", roles="ADMIN")
    @Test
    public void deleteMessage_worksAsIntended() throws Exception {

        doNothing().when(service).remove(any());

        mvc.perform(delete("/api/messages/abcdf-abcdef-abcdef-abcdef")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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

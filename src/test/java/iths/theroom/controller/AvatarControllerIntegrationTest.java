package iths.theroom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import iths.theroom.entity.AvatarEntity;
import iths.theroom.model.AvatarModel;
import iths.theroom.service.AvatarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
public class AvatarControllerIntegrationTest {



    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AvatarService avatarService;

    @Autowired
    ObjectMapper objectMapper;

    private MockMvc mvc;
    private AvatarEntity avatarEntity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        avatarEntity = new AvatarEntity();
        avatarEntity.setBase(1);
        avatarEntity.setHead(2);
        avatarEntity.setLegs(3);
        avatarEntity.setTorso(4);
    }

    @WithMockUser(username = "user")
    @Test
    public void getAllAvatars() throws Exception {

        avatarService.createAvatar(avatarEntity);

        ResultActions resultActions = mvc.perform(get("/api/avatars")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        ArrayNode jsonNode = objectMapper.readValue(contentAsString, ArrayNode.class);
        String uuid = jsonNode.get(0).get("uuid").asText();

        avatarService.deleteAvatar(uuid);
    }

    @WithMockUser(username = "user")
    @Test
    public void getAvatarById() throws Exception {

        AvatarModel avt = avatarService.createAvatar(avatarEntity);

        ResultActions resultActions =  mvc.perform(get("/api/avatars/" +avt.getUuid())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.torso", is(avatarEntity.getTorso())));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        AvatarModel avatarModel = objectMapper.readValue(contentAsString, AvatarModel.class);
        avatarService.deleteAvatar(avatarModel.getUuid());
    }

    @WithMockUser(username = "user")
    @Test
    public void getAvatarByIdNotFound_ThrowsNotFoundException() throws Exception {

        mvc.perform(get("/api/avatars/xxxx")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @WithMockUser(username = "user")
    @Test
    public void createAvatar_NormalOperation() throws Exception {

        ResultActions resultActions = mvc.perform(post("/api/avatars")
                .content(objectMapper.writeValueAsString(avatarEntity))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.torso", is(avatarEntity.getTorso())));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        AvatarModel avatarModel = objectMapper.readValue(contentAsString, AvatarModel.class);

        avatarService.deleteAvatar(avatarModel.getUuid());
    }

    @WithMockUser(username = "user")
    @Test
    public void updateAvatar() throws Exception  {

        AvatarModel avt = avatarService.createAvatar(avatarEntity);
        avatarEntity.setTorso(2);


        ResultActions resultActions = mvc.perform(put("/api/avatars/" +avt.getUuid())
                .content(objectMapper.writeValueAsString(avatarEntity))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.torso", is(avatarEntity.getTorso())));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        AvatarModel avatarModel = objectMapper.readValue(contentAsString, AvatarModel.class);

        avatarService.deleteAvatar(avatarModel.getUuid());
    }

    @WithMockUser(username = "user")
    @Test
    public void deleteAvatar() throws Exception {

        AvatarModel avatarModel = avatarService.createAvatar(avatarEntity);

        mvc.perform(delete("/api/avatars/" +avatarModel.getUuid())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

package iths.theroom.factory;

import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoomEntity;
import iths.theroom.model.MessageModel;
import iths.theroom.model.RoomModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class RoomFactoryTest {

    RoomFactory roomFactory;
    RoomEntity roomEntity1;
    RoomEntity roomEntity2;
    MessageEntity messageEntity1;
    MessageEntity messageEntity2;

    List<RoomEntity> roomEntities;

    @Before
    public void setUp(){
        roomFactory = new RoomFactory();

        messageEntity1 = new MessageEntity();
        messageEntity1.setContent("abc");

        messageEntity2 = new MessageEntity();
        messageEntity2.setContent("123");

        Set<MessageEntity> messageEntities = new HashSet<>();
        messageEntities.add(messageEntity1);
        messageEntities.add(messageEntity2);

        roomEntity1 = new RoomEntity("Room1", "Blue");
        roomEntity1.setMessages(messageEntities);

        roomEntity2 = new RoomEntity("Room2", "Red");

        roomEntities = new ArrayList<>();
        roomEntities.add(roomEntity1);
        roomEntities.add(roomEntity2);
    }

    @Test
    public void entityToModel_ReturnValidModel(){
        String expectedRoomName = roomEntity1.getRoomName();
        String expectedRoomColor = roomEntity1.getBackgroundColor();
        String expectedMessage1Content = messageEntity1.getContent();
        String expectedMessage2Content = messageEntity2.getContent();

        RoomModel result = roomFactory.entityToModel(roomEntity1);
        assertNotNull(result);
        Set<MessageModel> messages = result.getMessages();
        assertNotNull(messages);


        String actualRoomName = result.getRoomName();
        String actualRoomColor = result.getBackgroundColor();
        StringBuilder messageContents = new StringBuilder();
        for( MessageModel messageModel : messages) {
            messageContents.append(messageModel.getContent());
        }

        String actualMessageContent = messageContents.toString();
        assertEquals(expectedRoomName, actualRoomName);
        assertEquals(expectedRoomColor, actualRoomColor);
        assertTrue(actualMessageContent.contains(expectedMessage1Content));
        assertTrue(actualMessageContent.contains(expectedMessage2Content));
    }
}

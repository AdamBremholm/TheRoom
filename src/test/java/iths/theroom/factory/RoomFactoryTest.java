package iths.theroom.factory;

import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoomEntity;
import org.junit.Before;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoomFactoryTest {

    RoomFactory roomFactory;
    RoomEntity roomEntity1;
    RoomEntity roomEntity2;

    List<RoomEntity> roomEntities;

    @Before
    public void setUp(){
        roomFactory = new RoomFactory();

        MessageEntity messageEntity1 = new MessageEntity();
        messageEntity1.setContent("abc");

        MessageEntity messageEntity2 = new MessageEntity();
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
}

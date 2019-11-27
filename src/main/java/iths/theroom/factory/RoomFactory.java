package iths.theroom.factory;

import iths.theroom.entity.RoomEntity;
import iths.theroom.model.RoomModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomFactory {

    private final MessageFactory messageFactory;

    @Autowired
    public RoomFactory(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    public RoomModel entityToModel(RoomEntity roomEntity) {

        RoomModel roomModel = new RoomModel();

        roomModel.setRoomName(roomEntity.getRoomName());

        if(roomEntity.getMessages() != null){

            roomEntity.getMessages().forEach(messageEntity ->
                roomModel.getMessages().add(messageFactory.entityToModel(messageEntity))
            );
        }

        return roomModel;

    }
}

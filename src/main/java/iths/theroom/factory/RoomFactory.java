package iths.theroom.factory;

import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoomEntity;
import iths.theroom.model.MessageModel;
import iths.theroom.model.RoomModel;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class RoomFactory {

    public RoomModel entityToModel(RoomEntity roomEntity) {
        RoomModel roomModel = new RoomModel();
        roomModel.setRoomName(roomEntity.getRoomName());
        roomModel.setBackgroundColor(roomEntity.getBackgroundColor());

        if(roomEntity.getMessages() != null){
            for(MessageEntity message : roomEntity.getMessages()){
                MessageModel messageModel = MessageFactory.toModel(message);
                roomModel.getMessages().add(messageModel);
            }
        }
        return roomModel;
    }

    public List<RoomModel> entityToModel(List<RoomEntity> roomEntities) {

        List<RoomModel> roomModels = new ArrayList<>();

        if(roomEntities != null){
            roomEntities.forEach(roomEntity ->
                roomModels.add(entityToModel(roomEntity))
            );
        }
        return roomModels;
    }
}

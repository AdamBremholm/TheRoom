package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.model.MessageModel;
import iths.theroom.model.RoomModel;
import iths.theroom.pojos.MessageForm;

import java.util.List;

public interface RoomService {

    List<RoomModel> getAllRooms();

    RoomModel getOneModelByName(String name);

    RoomModel save(RoomEntity roomEntity);

    RoomModel updateRoom(String name, RoomEntity roomEntity);

    RoomEntity getOneEntityByName(String name);

    RoomModel deleteRoom(String name);

    RoomModel updateRoom(MessageForm messageForm);

    boolean isUserBannedHere(String userName, String roomName);

    List<MessageModel> getAllMessagesForRoom(String roomName);
}

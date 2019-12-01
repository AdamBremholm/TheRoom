package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.model.RoomModel;

import java.util.List;

public interface RoomService {

    List<RoomModel> getAllRooms();

    RoomModel getOneByName(String name);

    RoomModel save(RoomEntity roomEntity);

    RoomModel updateRoom(String name, RoomEntity roomEntity);

    RoomModel deleteRoom(String name);
}

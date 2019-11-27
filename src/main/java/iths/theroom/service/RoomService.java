package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.exception.RequestException;
import iths.theroom.model.RoomModel;

import java.util.List;

public interface RoomService {

    List<RoomModel> getAllRooms();

    RoomModel getOneByName(String name) throws RequestException;

    RoomModel save(RoomEntity roomEntity) throws RequestException;

    RoomModel updateRoom(String name, RoomEntity roomEntity) throws RequestException;

    RoomModel deleteRoom(String name) throws RequestException;
}

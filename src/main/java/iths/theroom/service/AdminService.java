package iths.theroom.service;

import iths.theroom.model.RoomModel;
import iths.theroom.model.UserModel;

public interface AdminService {

    RoomModel banUserFromRoom(String roomName, String userName);
    RoomModel removeBanFromUser(String roomName, String userName);
    RoomModel deleteRoom(String roomName);
    UserModel upgradeUserToAdmin(String userName);
}

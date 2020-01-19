package iths.theroom.service;

import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.ConflictException;
import iths.theroom.model.MessageModel;
import iths.theroom.model.RoomModel;
import iths.theroom.model.UserModel;

public interface AdminService {

    RoomModel banUserFromRoom(String roomName, String userName) throws BadRequestException;
    RoomModel removeBanFromUser(String roomName, String userName);
    RoomModel deleteRoom(String roomName);
    UserModel upgradeUserToAdmin(String userName) throws BadRequestException, ConflictException;
    MessageModel removeMessage(String uuid);
}

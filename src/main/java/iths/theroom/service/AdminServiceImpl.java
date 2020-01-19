package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.factory.RoomFactory;
import iths.theroom.model.MessageModel;
import iths.theroom.model.RoomModel;
import iths.theroom.model.UserModel;
import iths.theroom.repository.MessageRepository;
import iths.theroom.repository.RoomRepository;
import iths.theroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AdminServiceImpl implements AdminService{

    private RoomRepository roomRepository;
    private UserRepository userRepository;
    private MessageRepository messageRepository;
    private RoomFactory roomFactory;

    @Autowired
    public AdminServiceImpl(RoomRepository roomRepository, UserRepository userRepository, MessageRepository messageRepository, RoomFactory roomFactory) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.roomFactory = roomFactory;
    }

    @Override
    public RoomModel banUserFromRoom(String userName, String roomName) {
        RoomEntity room = roomRepository.findRoomByNameWithQuery(roomName);
        UserEntity user = userRepository.findUserByNameWithQuery(userName);
        Set<UserEntity> bannedUsers = room.getBannedUsers();
        Set<RoomEntity> excludedFromRooms = user.getExcludedRooms();
        bannedUsers.add(user);
        room.setBannedUsers(bannedUsers);
        excludedFromRooms.add(room);
        user.setExcludedRooms(excludedFromRooms);
        roomRepository.save(room);

        return roomFactory.entityToModel(room);
    }

    @Override
    public RoomModel removeBanFromUser(String userName, String roomName) {
        RoomEntity room = roomRepository.findRoomByNameWithQuery(roomName);
        UserEntity user = userRepository.findUserByNameWithQuery(userName);
        Set<UserEntity> bannedUsers = room.getBannedUsers();
        Set<RoomEntity> excludedFromRooms = user.getExcludedRooms();
        bannedUsers.remove(user);
        room.setBannedUsers(bannedUsers);
        excludedFromRooms.remove(room);
        user.setExcludedRooms(excludedFromRooms);
        roomRepository.save(room);

        return roomFactory.entityToModel(room);
    }

    @Override
    public RoomModel deleteRoom(String roomName) {
        RoomEntity room = roomRepository.findRoomByNameWithQuery(roomName);
        roomRepository.delete(room);

        return roomFactory.entityToModel(room);
    }

    @Override
    public UserModel upgradeUserToAdmin(String userName) {
        return null;
    }

    @Override
    public MessageModel removeMessage(String uuid) {
        return null;
    }
}

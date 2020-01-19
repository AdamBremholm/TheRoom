package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.ConflictException;
import iths.theroom.factory.RoomFactory;
import iths.theroom.model.MessageModel;
import iths.theroom.model.RoomModel;
import iths.theroom.model.UserModel;
import iths.theroom.repository.MessageRepository;
import iths.theroom.repository.RoomRepository;
import iths.theroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.Set;

import static iths.theroom.factory.UserFactory.toModel;

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
    public RoomModel banUserFromRoom(String userName, String roomName) throws BadRequestException {
        RoomEntity room = roomRepository.findRoomByNameWithQuery(roomName);
        UserEntity user = userRepository.findUserByNameWithQuery(userName);
        Set<UserEntity> bannedUsers;
        Set<RoomEntity> excludedFromRooms;
        if(room != null) {
            bannedUsers = room.getBannedUsers();
            bannedUsers.add(user);
            room.setBannedUsers(bannedUsers);
        } else {
            throw new BadRequestException("This room does not exist!");
        }
        if(user != null) {
            excludedFromRooms = user.getExcludedRooms();
            excludedFromRooms.add(room);
            user.setExcludedRooms(excludedFromRooms);
        } else {
            throw new BadRequestException("This user does not exist!");
        }
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
    public UserModel upgradeUserToAdmin(String userName) throws BadRequestException, ConflictException{
        UserEntity user = userRepository.findUserByNameWithQuery(userName);
        String roles;
        if(user != null) {
            roles = user.getRoles();
        } else {
            throw new BadRequestException("This user does not exist");
        }
        if (!roles.contains("ADMIN")) {
            roles += ",ADMIN";
        } else {
            throw new ConflictException("This user is already an administrator");
        }
        user.setRoles(roles);
        userRepository.save(user);

        return toModel(user);
    }

    @Override
    public MessageModel removeMessage(String uuid) {
        return null;
    }
}

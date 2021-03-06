package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.ConflictException;
import iths.theroom.exception.NotFoundException;
import iths.theroom.factory.RoomFactory;
import iths.theroom.model.RoomModel;
import iths.theroom.model.UserModel;
import iths.theroom.repository.RoomRepository;
import iths.theroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

import static iths.theroom.factory.UserFactory.toModel;

@Service
public class AdminServiceImpl implements AdminService{

    private RoomRepository roomRepository;
    private UserRepository userRepository;
    private RoomFactory roomFactory;

    @Autowired
    public AdminServiceImpl(RoomRepository roomRepository, UserRepository userRepository, RoomFactory roomFactory) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.roomFactory = roomFactory;
    }

    @Override
    public RoomModel banUserFromRoom(String userName, String roomName) {
        if(!userName.isBlank() && !roomName.isBlank()) {
            RoomEntity room = roomRepository.findRoomByNameWithQuery(roomName);
            UserEntity user = userRepository.findUserByNameWithQuery(userName);
            Set<UserEntity> bannedUsers;
            Set<RoomEntity> excludedFromRooms;
            if(room != null) {
                bannedUsers = room.getBannedUsers();
                bannedUsers.add(user);
                room.setBannedUsers(bannedUsers);
            } else {
                throw new NotFoundException("This room does not exist!");
            }
            if(user != null) {
                excludedFromRooms = user.getExcludedRooms();
                excludedFromRooms.add(room);
                user.setExcludedRooms(excludedFromRooms);
            } else {
                throw new NotFoundException("This user does not exist!");
            }
            roomRepository.save(room);

            return roomFactory.entityToModel(room);
        } else {
            throw new BadRequestException("No empty fields allowed!");
        }
    }

    @Override
    public RoomModel removeBanFromUser(String userName, String roomName) {
        if(!userName.isBlank() && !roomName.isBlank()) {
            RoomEntity room = roomRepository.findRoomByNameWithQuery(roomName);
            UserEntity user = userRepository.findUserByNameWithQuery(userName);
            Set<UserEntity> bannedUsers;
            Set<RoomEntity> excludedFromRooms;
            if(room != null) {
                bannedUsers = room.getBannedUsers();
                bannedUsers.remove(user);
                room.setBannedUsers(bannedUsers);
            } else {
                throw new NotFoundException("This room does not exist!");
            }
            if(user != null) {
                excludedFromRooms = user.getExcludedRooms();
                excludedFromRooms.remove(room);
                user.setExcludedRooms(excludedFromRooms);
            } else {
                throw new NotFoundException("This user does not exist!");
            }
            roomRepository.save(room);

            return roomFactory.entityToModel(room);
        } else {
            throw new BadRequestException("No empty fields allowed!");
        }
    }

    @Override
    public RoomModel deleteRoom(String roomName) {
        if(!roomName.isBlank()) {
            RoomEntity room = roomRepository.findRoomByNameWithQuery(roomName);
            if(room != null) {
                roomRepository.delete(room);
                return roomFactory.entityToModel(room);
            } else {
                throw new NotFoundException("This room does not exist");
            }
        } else {
            throw new BadRequestException("No empty fields allowed!");
        }
    }

    @Override
    public UserModel upgradeUserToAdmin(String userName) {
        if(!userName.isBlank()) {
            UserEntity user = userRepository.findUserByNameWithQuery(userName);
            String roles;
            if(user != null) {
                roles = user.getRoles();
            } else {
                throw new NotFoundException("This user does not exist");
            }
            if (!roles.contains("ADMIN")) {
                roles += ",ADMIN";
            } else {
                throw new ConflictException("This user is already an administrator");
            }
            user.setRoles(roles);
            userRepository.save(user);

            return toModel(user);
        } else {
            throw new BadRequestException("No empty fields allowed!");
        }
    }

}

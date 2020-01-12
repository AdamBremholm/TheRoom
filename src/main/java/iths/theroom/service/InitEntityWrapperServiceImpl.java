package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.NoSuchUserException;
import iths.theroom.model.RoomModel;
import iths.theroom.pojos.MessageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitEntityWrapperServiceImpl implements InitEntityWrapperService {

    private final UserService userService;
    private final RoomService roomService;

    @Autowired
    public InitEntityWrapperServiceImpl(UserService userService, RoomService roomService) {
        this.userService = userService;
        this.roomService = roomService;
    }

    @Override
    public void initRoomSession(MessageForm form) {
        try {
            userService.save(createOrFetchUser(form));
        }
        catch (Exception e){

        }
        try {
            RoomModel roomModel = roomService.save(createOrFetchRoom(form));
            form.setRoomBackgroundColor(roomModel.getBackgroundColor());
        }
        catch (BadRequestException e){
        }
    }

    private UserEntity createOrFetchUser(MessageForm form) {
        UserEntity user;
        try{
            return userService.getByUserName(form.getSender());
        }
        catch(NoSuchUserException e){
            user = new UserEntity();
            user.setUserName(form.getSender());
            user.setPassword(form.getSender());
            user.setPasswordConfirm(form.getSender());
            user.setEmail(form.getSender());
        }
        return user;
    }

    private RoomEntity createOrFetchRoom(MessageForm form) {
        RoomEntity room;
        try{
            return roomService.getOneByNameE(form.getRoomName());
        }
        catch(Exception e){
            room = new RoomEntity();
            room.setRoomName(form.getRoomName());
        }
        return room;
    }
    }
    
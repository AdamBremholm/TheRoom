package iths.theroom.controller;

import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.exception.NoSuchUserException;
import iths.theroom.exception.NotFoundException;
import iths.theroom.factory.MessageFactory;
import iths.theroom.model.MessageModel;
import iths.theroom.pojos.MessageForm;
import iths.theroom.service.MessageService;
import iths.theroom.service.RoomService;
import iths.theroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import java.util.Objects;

import static iths.theroom.factory.MessageFactory.toModel;

@Controller
public class WebSocketChatController {

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;


    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/javainuse")
    public MessageModel sendMessage(@Payload MessageForm messageForm) {
       return toModel(messageService.save(messageForm));
    }

    @MessageMapping("/chat.newUser")
    @SendTo("/topic/javainuse")
    public MessageModel newUser(@Payload MessageForm webSocketChatMessage, SimpMessageHeaderAccessor headerAccessor) {
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", webSocketChatMessage.getSender());
        moveToServiceClassLater__InitRoomSession(webSocketChatMessage);
        return MessageFactory.toModel(webSocketChatMessage);
    }

    private void moveToServiceClassLater__InitRoomSession(MessageForm form){
        userService.save(moveToServiceClassLater__CreateOrFetchUser(form));
        roomService.save(moveToServiceClassLater__CreateOrFetchRoom(form));
    }

    private UserEntity moveToServiceClassLater__CreateOrFetchUser(MessageForm form){
        UserEntity user;
        try{
          user = userService.getByUserName(form.getSender());
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

    private RoomEntity moveToServiceClassLater__CreateOrFetchRoom(MessageForm form){
        RoomEntity room;
        try{
            room = roomService.getOneByNameE(form.getRoomName());
        }
        catch(NotFoundException e){
            room = new RoomEntity();
            room.setRoomName(form.getRoomName());
        }
        return room;
    }
}
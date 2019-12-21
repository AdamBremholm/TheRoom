package iths.theroom.controller;

import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.enums.Type;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.NoSuchUserException;
import iths.theroom.factory.MessageFactory;
import iths.theroom.model.MessageModel;
import iths.theroom.model.RoomModel;
import iths.theroom.pojos.MessageForm;
import iths.theroom.service.MessageService;
import iths.theroom.service.RoomService;
import iths.theroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import java.util.Objects;

@Controller
public class WebSocketChatController {

    private final MessageService messageService;
    private final UserService userService;
    private final RoomService roomService;

    @Autowired
    public WebSocketChatController(MessageService messageService, UserService userService, RoomService roomService) {
        this.messageService = messageService;
        this.userService = userService;
        this.roomService = roomService;
    }


    @MessageMapping("/chat.sendMessage.{roomName}")
    @SendTo("/topic/{roomName}")
    public MessageModel sendMessage(@DestinationVariable String roomName, @Payload MessageForm messageForm) {
        if(messageForm.getType()== Type.BG_CHANGE) {
           roomService.updateRoom(messageForm);
        }
        return messageService.save(messageForm);
    }

    @MessageMapping("/chat.newUser.{roomName}")
    @SendTo("/topic/{roomName}")
    public MessageModel newUser(@DestinationVariable String roomName, @Payload MessageForm webSocketChatMessage, SimpMessageHeaderAccessor headerAccessor) {
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", webSocketChatMessage.getSender());
        moveToServiceClassLater__InitRoomSession(webSocketChatMessage);
        return MessageFactory.toModel(webSocketChatMessage);
    }

    private void moveToServiceClassLater__InitRoomSession(MessageForm form){
        try {
            userService.save(moveToServiceClassLater__CreateOrFetchUser(form));
        }
        catch (Exception e){
        }
        try {
           RoomModel roomModel = roomService.save(moveToServiceClassLater__CreateOrFetchRoom(form));
           form.setRoomBackgroundColor(roomModel.getBackgroundColor());
        }
        catch (BadRequestException e){

        }
    }


    private UserEntity moveToServiceClassLater__CreateOrFetchUser(MessageForm form){
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

    private RoomEntity moveToServiceClassLater__CreateOrFetchRoom(MessageForm form){
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

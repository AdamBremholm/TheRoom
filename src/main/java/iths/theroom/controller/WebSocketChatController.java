package iths.theroom.controller;

import iths.theroom.exception.NotFoundException;
import iths.theroom.factory.MessageFactory;
import iths.theroom.model.MessageModel;
import iths.theroom.pojos.MessageForm;
import iths.theroom.service.MessageService;
import iths.theroom.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

@Controller
public class WebSocketChatController {

    private final MessageService messageService;
    private final RoomService roomService;

    @Autowired
    public WebSocketChatController(MessageService messageService, RoomService roomService) {
        this.messageService = messageService;
        this.roomService = roomService;
    }

    @MessageMapping("/chat.sendMessage.{roomName}")
    @SendTo("/topic/chatMessages.{roomName}")
    public MessageModel sendMessage(@DestinationVariable String roomName, @Payload MessageForm messageForm) {

        return messageService.save(messageForm);
    }

    @MessageMapping("/chat.changeBgColor.{roomName}")
    @SendTo("/topic/backgroundChange.{roomName}")
    public String changeBackground(@DestinationVariable String roomName, @Payload MessageForm messageForm) {
        roomService.updateRoom(messageForm);
        return messageForm.getRoomBackgroundColor();
    }

    @MessageMapping("/chat.increaseRating.{messageUuid}.{roomName}")
    @SendTo("/topic/rating.{roomName}")
    public MessageModel increaseRating(@DestinationVariable String messageUuid, Authentication authentication){
//        String userName = authentication.getName();
//        return messageService.increaseMessageRating(messageUuid, userName);

        throw new NotFoundException("Hejhej");
    }

    @MessageMapping("/chat.decreaseRating.{messageUuid}.{roomName}")
    @SendTo("/topic/rating.{roomName}")
    public MessageModel decreaseRating(@DestinationVariable String messageUuid, Authentication authentication){
        String userName = authentication.getName();
        return messageService.decreaseMessageRating(messageUuid, userName);
    }

    @MessageMapping("/chat.retrieveAll.{userName}.{roomName}")
    @SendTo("/topic/{userName}.{roomName}")
    public List<MessageModel> getAllMessages(@DestinationVariable String roomName){
            return messageService.findAllByRoomEntityOrderById(roomName);
    }

    @MessageMapping("/chat.newUser.{roomName}")
    @SendTo("/topic/alerts.{roomName}")
    public MessageModel newUser(@DestinationVariable String roomName, @Payload MessageForm webSocketChatMessage, SimpMessageHeaderAccessor headerAccessor) {
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", webSocketChatMessage.getSender());
        return MessageFactory.toModel(webSocketChatMessage);
    }
}

package iths.theroom.controller;

import iths.theroom.enums.Type;
import iths.theroom.factory.MessageFactory;
import iths.theroom.model.MessageModel;
import iths.theroom.pojos.MessageForm;
import iths.theroom.service.InitEntityWrapperService;
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
import java.util.Objects;

@Controller
public class WebSocketChatController {

    private final MessageService messageService;
    private final RoomService roomService;
    private final InitEntityWrapperService initEntityWrapperService;

    @Autowired
    public WebSocketChatController(MessageService messageService, RoomService roomService, InitEntityWrapperService initEntityWrapperService) {
        this.messageService = messageService;
        this.roomService = roomService;
        this.initEntityWrapperService = initEntityWrapperService;
    }

    @MessageMapping("/chat.sendMessage.{roomName}")
    @SendTo("/topic/{roomName}")
    public MessageModel sendMessage(@DestinationVariable String roomName, @Payload MessageForm messageForm) {
        if(messageForm.getType()== Type.BG_CHANGE) {
           roomService.updateRoom(messageForm);
        }
        return messageService.save(messageForm);
    }

    @MessageMapping("/chat.increaseRating.{messageUuid}")
    @SendTo("/topic/{messageUuid}")
    public MessageModel increaseRating(@DestinationVariable String messageUuid, Authentication authentication){
        String userName = authentication.getName();
        return messageService.increaseMessageRating(messageUuid, userName);
    }

    @MessageMapping("/chat.decreaseRating.{messageUuid}")
    @SendTo("/topic/{messageUuid}")
    public MessageModel decreaseRating(@DestinationVariable String messageUuid, Authentication authentication){
        String userName = authentication.getName();
        return messageService.decreaseMessageRating(messageUuid, userName);
    }

    @MessageMapping("/chat.newUser.{roomName}")
    @SendTo("/topic/{roomName}")
    public MessageModel newUser(@DestinationVariable String roomName, @Payload MessageForm webSocketChatMessage, SimpMessageHeaderAccessor headerAccessor) {
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", webSocketChatMessage.getSender());
        initEntityWrapperService.initRoomSession(webSocketChatMessage);
        return MessageFactory.toModel(webSocketChatMessage);
    }
}

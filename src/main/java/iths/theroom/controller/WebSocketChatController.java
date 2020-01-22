package iths.theroom.controller;

import iths.theroom.entity.RoomEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.NotFoundException;
import iths.theroom.exception.UnauthorizedException;
import iths.theroom.factory.MessageFactory;
import iths.theroom.model.RoomModel;
import iths.theroom.pojos.MessageForm;
import iths.theroom.service.MessageService;
import iths.theroom.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

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
    public ResponseEntity sendMessage(@DestinationVariable String roomName, @Payload MessageForm messageForm,
                                      Authentication authentication) {
        String userName = authentication.getName();
        try{
            roomService.isUserBannedHere(userName, roomName);
            return ResponseEntity.ok(messageService.save(messageForm));

        } catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("User", userName).body(e.getMessage());
        } catch (BadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("User", userName).body(e.getMessage());
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("User", userName).body(e.getMessage());
        }

    }

    @MessageMapping("/chat.changeBgColor.{roomName}")
    @SendTo("/topic/backgroundChange.{roomName}")
    public ResponseEntity changeBackground(@Payload MessageForm messageForm, Authentication authentication) {
        String userName = authentication.getName();

        try{
            roomService.isUserBannedHere(userName, messageForm.getRoomName());
            RoomModel roomModel = roomService.updateRoom(messageForm);
            return ResponseEntity.ok(roomModel.getBackgroundColor());

        } catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("User", userName).body(e.getMessage());
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("User", userName).body(e.getMessage());
        }

    }

    @MessageMapping("/chat.increaseRating.{messageUuid}.{roomName}")
    @SendTo("/topic/rating.{roomName}")
    public ResponseEntity increaseRating(@DestinationVariable String messageUuid, @DestinationVariable String roomName,
                                         Authentication authentication){
        String userName = authentication.getName();
        try{
            roomService.isUserBannedHere(userName, roomName);
            return ResponseEntity.ok(messageService.increaseMessageRating(messageUuid, userName));

        } catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("User", userName).body(e.getMessage());
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("User", userName).body(e.getMessage());
        }
    }

    @MessageMapping("/chat.decreaseRating.{messageUuid}.{roomName}")
    @SendTo("/topic/rating.{roomName}")
    public ResponseEntity decreaseRating(@DestinationVariable String messageUuid, @DestinationVariable String roomName,
                                         Authentication authentication){
        String userName = authentication.getName();
        try{
            roomService.isUserBannedHere(userName, roomName);
            return ResponseEntity.ok(messageService.decreaseMessageRating(messageUuid, userName));

        } catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("User", userName).body(e.getMessage());
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("User", userName).body(e.getMessage());
        }
    }

    @MessageMapping("/chat.retrieveAll.{userName}.{roomName}")
    @SendTo("/topic/{userName}.{roomName}")
    public ResponseEntity getAllMessages(@DestinationVariable String roomName, Authentication authentication){

        String userName = authentication.getName();
        try{
            roomService.save(new RoomEntity(roomName));
            roomService.isUserBannedHere(userName, roomName);
            return ResponseEntity.ok(messageService.findAllByRoomEntityOrderById(roomName));

        } catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("User", userName).body(e.getMessage());
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("User", userName).body(e.getMessage());
        }

    }

    @MessageMapping("/chat.newUser.{roomName}")
    @SendTo("/topic/alerts.{roomName}")
    public ResponseEntity newUser(@DestinationVariable String roomName, @Payload MessageForm messageForm,
                                  Authentication authentication) {
        String userName = authentication.getName();
        try{
            roomService.isUserBannedHere(userName, roomName);
            return ResponseEntity.ok(MessageFactory.toModel(messageForm));

        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("User", userName).body(e.getMessage());
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("User", userName).body(e.getMessage());
        }

    }
}

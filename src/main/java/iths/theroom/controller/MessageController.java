package iths.theroom.controller;

import iths.theroom.model.MessageModel;
import iths.theroom.pojos.MessageForm;
import iths.theroom.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/messages")
    public List<MessageModel> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{uuid}")
    public MessageModel getMessage(@PathVariable("uuid") String uuid) {
        return messageService.getMessageByUuid(uuid);
    }

    @PostMapping("/messages")
    public MessageModel addMessage(@RequestBody MessageForm form) {
        return messageService.save(form);
    }

    @DeleteMapping("/messages/{uuid}")
    public void deleteMessage(@PathVariable("uuid") String uuid) {
        messageService.remove(uuid);
    }

    @GetMapping("/messages/search")
    public List<MessageModel> getLastMessagesFromUserInRoom(@RequestParam(name ="username") String userName,
                                                            @RequestParam(required = false, name ="roomname") String roomName,
                                                            @RequestParam(required = false, name ="count") String count) {
        return messageService.getAllMessagesFromUser(userName, roomName, count);
    }

    @PutMapping("/messages/decRating.{uuid}")
    public int decreaseMessageRating(@PathVariable String uuid){
        return messageService.decreaseMessageRating(uuid);
    }

    @PutMapping("/messages/incRating.{uuid}")
    public int increaseMessageRating(@PathVariable String uuid){
        return messageService.increaseMessageRating(uuid);
    }

}

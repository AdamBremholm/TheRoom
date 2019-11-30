package iths.theroom.controller;

import iths.theroom.entity.MessageEntity;
import iths.theroom.model.MessageModel;
import iths.theroom.pojos.MessageForm;
import iths.theroom.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

import static iths.theroom.factory.MessageFactory.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/messages")
    public List<MessageModel> getAllMessages() {
        return toModel(messageService.getAllMessages());
    }

    @GetMapping("/messages/{uuid}")
    public MessageModel getMessage(@PathVariable("uuid") String uuid) {
            MessageEntity found = messageService.getMessageByUuid(uuid);
            return toModel(found);
    }

    @PostMapping("/messages")
    public MessageModel addMessage(@RequestBody MessageForm form) {
        return toModel(messageService.save(form));
    }

    @DeleteMapping("/messages/{uuid}")
    public void deleteMessage(@PathVariable("uuid") String uuid) {
        messageService.remove(uuid);
    }

    @GetMapping("/messages/search")
    public List<MessageModel> getLastMessagesFromUserInRoom(@RequestParam(name ="username") String userName,@RequestParam(required = false, name ="roomname") String roomName, @RequestParam(required = false, name ="count") int count) {
        return toModel(messageService.getAllMessages());
    }
}

package iths.theroom.controller;

import iths.theroom.entity.Message;
import iths.theroom.model.MessageModel;
import iths.theroom.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static iths.theroom.factory.MessageFactory.toModel;

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
            Message found = messageService.getMessageByUuid(uuid);
            return toModel(found);
    }

    @PostMapping("/messages")
    public MessageModel addMessage(@RequestBody Message message) {
        return toModel(messageService.save(message));
    }
    @DeleteMapping("/messages/{uuid}")
    public void deleteMessage(@PathVariable("uuid") String uuid) {
        messageService.remove(uuid);
    }
}

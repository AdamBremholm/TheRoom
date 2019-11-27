package iths.theroom.controller;

import iths.theroom.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/theroom")
public class RoomController {

    private final MessageService messageService;

    @Autowired
    public RoomController(MessageService messageService) {
        this.messageService = messageService;
    }
}

package iths.theroom.controller;

import iths.theroom.model.MessageModel;
import iths.theroom.model.RoomModel;
import iths.theroom.model.UserModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    public RoomModel banUserFromRoom() {
        return null;
    }

    public RoomModel removeBanFromUser() {
        return null;
    }

    public RoomModel deleteRoom() {
        return null;
    }

    public UserModel upgradeUserToAdmin() {
        return null;
    }

    public MessageModel removeMessageFromFeed() {
        return null;
    }


}

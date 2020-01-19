package iths.theroom.controller;

import iths.theroom.model.MessageModel;
import iths.theroom.model.RoomModel;
import iths.theroom.model.UserModel;
import iths.theroom.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PutMapping("/ban")
    public RoomModel banUserFromRoom(@RequestParam(name="username") String userName,
                                     @RequestParam(name="roomname") String roomName) {
        return adminService.banUserFromRoom(userName, roomName);
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

package iths.theroom.controller;

import iths.theroom.model.MessageModel;
import iths.theroom.model.RoomModel;
import iths.theroom.model.UserModel;
import iths.theroom.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/unban")
    public RoomModel removeBanFromUser(@RequestParam(name="username") String userName,
                                       @RequestParam(name="roomname") String roomName) {
        return adminService.removeBanFromUser(userName, roomName);
    }

    @DeleteMapping("/delete")
    public RoomModel deleteRoom(@RequestParam(name="roomname") String roomName) {
        return adminService.deleteRoom(roomName);
    }

    public UserModel upgradeUserToAdmin() {
        return null;
    }

    public MessageModel removeMessageFromFeed() {
        return null;
    }


}

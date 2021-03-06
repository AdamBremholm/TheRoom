package iths.theroom.controller;

import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.ConflictException;
import iths.theroom.exception.NotFoundException;
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
                                     @RequestParam(name="roomname") String roomName) throws NotFoundException, BadRequestException {
        return adminService.banUserFromRoom(userName, roomName);
    }

    @PutMapping("/unban")
    public RoomModel removeBanFromUser(@RequestParam(name="username") String userName,
                                       @RequestParam(name="roomname") String roomName) throws NotFoundException, BadRequestException {
        return adminService.removeBanFromUser(userName, roomName);
    }

    @DeleteMapping("/delete")
    public RoomModel deleteRoom(@RequestParam(name="roomname") String roomName) throws NotFoundException, BadRequestException {
        return adminService.deleteRoom(roomName);
    }

    @PutMapping("/upgrade")
    public UserModel upgradeUserToAdmin(@RequestParam("username") String userName) throws NotFoundException, ConflictException, BadRequestException {
        return adminService.upgradeUserToAdmin(userName);
    }

}

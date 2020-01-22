package iths.theroom.controller;

import iths.theroom.entity.UserEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.ConflictException;
import iths.theroom.model.UserModel;
import iths.theroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static iths.theroom.factory.UserFactory.toModel;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserModel> getAllMessages() {
        return toModel(userService.getAll());
    }

    @PostMapping("/users")
    public UserModel addMessage(@RequestBody UserEntity userEntity) throws ConflictException, BadRequestException {
        return toModel(userService.save(userEntity));
    }
}

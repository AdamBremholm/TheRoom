package iths.theroom.controller;

import iths.theroom.model.AvatarModel;
import iths.theroom.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AvatarController {

    @Autowired
    private AvatarService avatarService;

    @GetMapping("/avatars")
    public List<AvatarModel> getAllAvatars() {
        return avatarService.getAllAvatars();
    }

    

}

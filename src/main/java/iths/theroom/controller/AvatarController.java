package iths.theroom.controller;

import iths.theroom.entity.AvatarEntity;
import iths.theroom.model.AvatarModel;
import iths.theroom.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AvatarController {

    @Autowired
    private AvatarService avatarService;

    @GetMapping("/avatars")
    public List<AvatarModel> getAllAvatars() {
        return avatarService.getAllAvatars();
    }

    @GetMapping(value="/{avatarId}")
    public Optional<AvatarModel> getAvatarById(@PathVariable(name="avatarId") Long id) {
        return avatarService.getAvatarById(id);
    }

    @PostMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public AvatarModel createAvatar(@RequestBody AvatarEntity avatar) {
        return avatarService.createAvatar(avatar);
    }

    @PutMapping(value="/update", produces=MediaType.APPLICATION_JSON_VALUE)
    public AvatarModel updateAvatar(@RequestBody AvatarEntity avatar) {
        return avatarService.updateAvatar(avatar);
    }

}

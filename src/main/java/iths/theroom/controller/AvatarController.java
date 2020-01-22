package iths.theroom.controller;

import iths.theroom.entity.AvatarEntity;
import iths.theroom.model.AvatarModel;
import iths.theroom.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/avatars")
public class AvatarController {

    @Autowired
    private AvatarService avatarService;

    @GetMapping
    public List<AvatarModel> getAllAvatars() {
        return avatarService.getAllAvatars();
    }

    @GetMapping(value="/{uuid}")
    public AvatarModel getAvatarById(@PathVariable(name="uuid") String uuid) {
        return avatarService.findByUuid(uuid);
    }

    @PostMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public AvatarModel createAvatar(@RequestBody AvatarEntity avatar) {
        return avatarService.createAvatar(avatar);
    }

    @PutMapping(value="/{uuid}", produces=MediaType.APPLICATION_JSON_VALUE)
    public AvatarModel updateAvatar(@RequestBody AvatarEntity avatar, @PathVariable(name="uuid") String uuid) {
        return avatarService.updateAvatar(uuid, avatar);
    }

    @DeleteMapping(value="/{uuid}")
    public void deleteAvatar(@PathVariable(name="uuid")String uuid) {
        avatarService.deleteAvatar(uuid);
    }

}

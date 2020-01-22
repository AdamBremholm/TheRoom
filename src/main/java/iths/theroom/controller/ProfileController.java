package iths.theroom.controller;

import iths.theroom.exception.BadRequestException;
import iths.theroom.model.ProfileModel;
import iths.theroom.pojos.ProfileForm;
import iths.theroom.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path="/api")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/profile")
    public ProfileModel addProfile(HttpServletRequest req, @RequestBody ProfileForm form) {
        return profileService.save(form, req);
    }

    @GetMapping("/profile/{username}")
    public ProfileModel getProfile(@PathVariable()String username) {
        if(username != null && !username.isBlank()) {
            return profileService.get(username);
        }
        throw new BadRequestException("fill in the username");
        }
}


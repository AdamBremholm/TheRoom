package iths.theroom.controller;

import iths.theroom.model.ProfileModel;
import iths.theroom.pojos.ProfileForm;
import iths.theroom.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path="/theroom/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }


    @PostMapping("/profile")
    public ProfileModel addMessage(HttpServletRequest req, @RequestBody ProfileForm form) {
        return profileService.save(form, req);
    }

    }


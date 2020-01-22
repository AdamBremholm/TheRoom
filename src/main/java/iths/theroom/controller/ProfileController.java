package iths.theroom.controller;

import iths.theroom.model.ProfileModel;
import iths.theroom.pojos.ProfileForm;
import iths.theroom.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ProfileModel addMessage(HttpServletRequest req, @RequestBody ProfileForm form) {
        return profileService.save(form, req);
    }
}


package iths.theroom.controller;

import iths.theroom.entity.ProfileEntity;
import iths.theroom.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/theroom/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping(path="/")
    public ResponseEntity getAll() {

        return ResponseEntity.ok(null);
    }

    @GetMapping(path="/{email}")
    public ResponseEntity getOneByEmail(@PathVariable("email") String email) {

        return ResponseEntity.ok(null);
    }

    @PutMapping(path = "/{email}")
    public ResponseEntity updateProfile(@RequestBody ProfileEntity profileEntity, @PathVariable("email") String email) {
        return ResponseEntity.ok(null);
    }

    }


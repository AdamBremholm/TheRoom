package iths.theroom.service;

import iths.theroom.model.ProfileModel;
import iths.theroom.pojos.ProfileForm;

import javax.servlet.http.HttpServletRequest;

public interface ProfileService {

        ProfileModel save(ProfileForm form, HttpServletRequest req);
        ProfileModel get(String username);
}

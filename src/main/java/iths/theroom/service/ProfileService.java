package iths.theroom.service;

import iths.theroom.model.ProfileModel;

public interface ProfileService {
        ProfileModel getProfile(String email);
        ProfileModel getProfiles();
        ProfileModel setAboutMe(String email, String aboutMe);
        ProfileModel setGender(String email, String gender);
        ProfileModel setAge(String email, int age);
        ProfileModel setCountry(String email, String country);
        ProfileModel setStarSign(String email, String starSign);
        ProfileModel incrementVisitor(String email);
}
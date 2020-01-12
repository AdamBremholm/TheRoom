package iths.theroom.factory;

import iths.theroom.entity.ProfileEntity;
import iths.theroom.model.ProfileModel;
import org.springframework.stereotype.Component;


@Component
public class ProfileFactory {

    public static ProfileModel toModel(ProfileEntity profileEntity){
        ProfileModel profileModel = new ProfileModel();
        profileModel.setAboutMe(profileEntity.getAboutMe());
        profileModel.setAge(profileEntity.getAge());
        profileModel.setCountry(profileEntity.getCountry());
        profileModel.setGender(profileEntity.getGender());
        profileModel.setStarSign(profileEntity.getStarSign());
        return profileModel;
    }
}
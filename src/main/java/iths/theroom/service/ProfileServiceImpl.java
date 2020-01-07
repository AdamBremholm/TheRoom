package iths.theroom.service;

import iths.theroom.model.ProfileModel;
import iths.theroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;

    @Autowired
    public ProfileServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ProfileModel getProfile(String email) {
        return null;
    }

    @Override
    public ProfileModel getProfiles() {
        return null;
    }

    @Override
    public ProfileModel setAboutMe(String email, String aboutMe) {
        return null;
    }

    @Override
    public ProfileModel setGender(String email, String gender) {
        return null;
    }

    @Override
    public ProfileModel setAge(String email, int age) {
        return null;
    }

    @Override
    public ProfileModel setCountry(String email, String country) {
        return null;
    }

    @Override
    public ProfileModel setStarSign(String email, String starSign) {
        return null;
    }

    @Override
    public ProfileModel incrementVisitor(String email) {
        return null;
    }
}


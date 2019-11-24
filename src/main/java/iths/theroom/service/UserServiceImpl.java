package iths.theroom.service;

import iths.theroom.entity.UserEntity;
import iths.theroom.factory.UserFactory;
import iths.theroom.model.UserModel;
import iths.theroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserFactory userFactory;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserFactory userFactory) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
    }

    @Override
    public UserModel getUserById(Long id) throws RuntimeException {

        Optional<UserEntity> userEntityFound = userRepository.findById(id);

        if(userEntityFound.isPresent()){
            return userFactory.entityToModel(userEntityFound.get());
        }

        throw new RuntimeException("No entity found");

    }
}

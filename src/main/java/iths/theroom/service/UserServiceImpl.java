package iths.theroom.service;

import iths.theroom.entity.RoleEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.exception.NoSuchUserException;
import iths.theroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserEntity getUserById(Long id) throws RuntimeException {
         return userRepository.findById(id).orElseThrow(NoSuchUserException::new);
    }


    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity getByUserName(String userName) {
       return userRepository.findByUserName(userName).orElseThrow(NoSuchUserException::new);
    }

    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

}

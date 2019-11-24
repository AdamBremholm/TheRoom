package iths.theroom.dao;

import iths.theroom.entity.UserEntity;
import iths.theroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDao implements Dao<UserEntity> {

    private final UserRepository userRepository;

    @Autowired
    public UserDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserEntity> findOne(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
        userRepository.flush();
    }

    @Override
    public void delete(UserEntity userEntity) {
        userRepository.delete(userEntity);
    }
}

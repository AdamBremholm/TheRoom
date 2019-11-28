package iths.theroom.service;

import iths.theroom.entity.UserEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.ConflictException;
import iths.theroom.exception.NoSuchUserException;
import iths.theroom.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserEntity getUserById(Long id) {
         return userRepository.findById(id).orElseThrow(NoSuchUserException::new);
    }


    @Override
    public UserEntity save(UserEntity userEntity) throws ConflictException, BadRequestException {
        validateUserEntity(userEntity);
        checkForDuplicates(userEntity);
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

    @Override
    public void validatePassword(UserEntity userEntity) throws BadRequestException {
        if(userEntity!=null) {
            if (!userEntity.getPassword().equals(userEntity.getPasswordConfirm()))
                throw new BadRequestException("passwords does not match");

        }

    }
    @Override
    public void validateUserEntity(UserEntity userEntity) throws BadRequestException {
        Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userEntity);
        optionalUserEntity.map(UserEntity::getUserName)
                .filter(Predicate.not(String::isBlank)).orElseThrow(() -> new BadRequestException("username must be provided"));
        optionalUserEntity.map(UserEntity::getEmail)
                .filter(Predicate.not(String::isBlank)).orElseThrow(() -> new BadRequestException("email must be provided"));
        optionalUserEntity.map(UserEntity::getPassword)
                .filter(Predicate.not(String::isBlank)).orElseThrow(() -> new BadRequestException("password must be provided"));
        optionalUserEntity.map(UserEntity::getPasswordConfirm)
                .filter(Predicate.not(String::isBlank)).orElseThrow(() -> new BadRequestException("password confirmation must be provided"));
        validatePassword(optionalUserEntity.get());
    }

    @Override
    public void checkForDuplicates(UserEntity userEntity) throws ConflictException {
        Optional<UserEntity> foundUserByUserName = userRepository.findByUserName(userEntity.getUserName());
        if(foundUserByUserName.isPresent())
            throw new ConflictException("username: "+userEntity.getUserName() +" is already taken");
        Optional<UserEntity> foundUserEmail = userRepository.findByEmail(userEntity.getEmail());
        if(foundUserEmail.isPresent())
            throw new ConflictException("email: "+userEntity.getEmail() +" is already taken");
    }


}

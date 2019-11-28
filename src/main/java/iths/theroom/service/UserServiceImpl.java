package iths.theroom.service;

import iths.theroom.entity.UserEntity;
import iths.theroom.exception.BadRequestBetaException;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.ConflictException;
import iths.theroom.exception.NoSuchUserException;
import iths.theroom.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public void validateUserEntity(UserEntity userEntity) throws BadRequestBetaException {
        Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userEntity);
        optionalUserEntity.map(UserEntity::getUserName)
                .filter(Predicate.not(String::isBlank)).orElseThrow(() -> new BadRequestBetaException("username must be provided"));
        optionalUserEntity.map(UserEntity::getEmail)
                .filter(Predicate.not(String::isBlank)).orElseThrow(() -> new BadRequestBetaException("email must be provided"));
        optionalUserEntity.map(UserEntity::getPassword)
                .filter(Predicate.not(String::isBlank)).orElseThrow(() -> new BadRequestBetaException("password must be provided"));
        optionalUserEntity.map(UserEntity::getPasswordConfirm)
                .filter(Predicate.not(String::isBlank)).orElseThrow(() -> new BadRequestBetaException("password confirmation must be provided"));
        validatePassword(optionalUserEntity.get());
        encodePassword(optionalUserEntity.get());
    }

    @Override
    public void validatePassword(UserEntity userEntity) throws BadRequestBetaException {
        if(userEntity!=null) {
            if (!userEntity.getPassword().equals(userEntity.getPasswordConfirm()))
                throw new BadRequestBetaException("passwords does not match");

        }

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

    @Override
    public void encodePassword(UserEntity userEntity){
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
    }




}

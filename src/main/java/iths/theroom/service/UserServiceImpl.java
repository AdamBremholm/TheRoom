package iths.theroom.service;

import iths.theroom.entity.UserEntity;
import iths.theroom.exception.*;
import iths.theroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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

    private void validateUserEntity(UserEntity userEntity) throws BadRequestException {
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
        encodePassword(optionalUserEntity.get());
    }

    private void validatePassword(UserEntity userEntity) throws BadRequestException {
        if(userEntity!=null) {
            if (!userEntity.getPassword().equals(userEntity.getPasswordConfirm()))
                throw new BadRequestException("passwords does not match");

        }

    }

    private void checkForDuplicates(UserEntity userEntity) throws ConflictException {
        Optional<UserEntity> foundUserByUserName = userRepository.findByUserName(userEntity.getUserName());
        if(foundUserByUserName.isPresent())
            throw new ConflictException("username: "+userEntity.getUserName() +" is already taken");
        Optional<UserEntity> foundUserEmail = userRepository.findByEmail(userEntity.getEmail());
        if(foundUserEmail.isPresent())
            throw new ConflictException("email: "+userEntity.getEmail() +" is already taken");
    }

    private void encodePassword(UserEntity userEntity){
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws NotFoundException {
        Optional<UserEntity> optUserEntity = userRepository.findByUserName(userName);
        if (optUserEntity.isEmpty()) {
            throw new NotFoundException("User not found with username: " + userName);
        }
        return new org.springframework.security.core.userdetails.User(optUserEntity.get().getUserName(), optUserEntity.get().getPassword(),
                new ArrayList<>());
    }

    @Override
    public boolean isUserWhoItClaimsToBe(String username, HttpServletRequest req) {

        String requestingUserName = req.getUserPrincipal().getName();
        if(requestingUserName.equals(username)){
            return true;
        }
        return false;
    }

    @Override
    public UserEntity updateUser(UserEntity userEntity) {
        try {
            userRepository.save(userEntity);
        }
        catch(Exception e){
            throw new ConflictException("Internal server error");
        }
        return getByUserName(userEntity.getUserName());
    }
}

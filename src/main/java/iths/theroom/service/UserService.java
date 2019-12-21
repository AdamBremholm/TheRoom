package iths.theroom.service;

import iths.theroom.entity.UserEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.ConflictException;
import iths.theroom.exception.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserEntity getUserById(Long id);

    UserEntity save(UserEntity userEntity) throws ConflictException, BadRequestException;

    UserEntity getByUserName(String userName);

    List<UserEntity> getAll();

    void validatePassword(UserEntity userEntity) throws BadRequestException;

    void validateUserEntity(UserEntity userEntity) throws BadRequestException;

    void checkForDuplicates(UserEntity userEntity) throws ConflictException;

    void encodePassword(UserEntity userEntity);

    UserDetails loadUserByUsername(String userName) throws NotFoundException;
}

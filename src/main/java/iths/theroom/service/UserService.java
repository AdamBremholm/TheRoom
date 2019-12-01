package iths.theroom.service;

import iths.theroom.entity.UserEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.ConflictException;

import java.util.List;

public interface UserService {

    UserEntity getUserById(Long id);

    UserEntity save(UserEntity userEntity) throws ConflictException, BadRequestException;

    UserEntity getByUserName(String userName);

    List<UserEntity> getAll();

    void validatePassword(UserEntity userEntity) throws BadRequestException;

    void validateUserEntity(UserEntity userEntity) throws BadRequestException;

    void checkForDuplicates(UserEntity userEntity) throws ConflictException;

    public void encodePassword(UserEntity userEntity);
}

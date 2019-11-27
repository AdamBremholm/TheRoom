package iths.theroom.service;

import iths.theroom.entity.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity getUserById(Long id);

    UserEntity save(UserEntity userEntity);

    UserEntity getByUserName(String userName);

    List<UserEntity> getAll();
}

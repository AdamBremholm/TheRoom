package iths.theroom.service;

import iths.theroom.model.UserModel;

public interface UserService {

    UserModel getUserById(Long id) throws RuntimeException;
}

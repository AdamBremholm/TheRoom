package iths.theroom.service;

import iths.theroom.entity.UserEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.ConflictException;
import iths.theroom.exception.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends UserDetailsService {

    UserEntity getUserById(Long id);

    UserEntity save(UserEntity userEntity) throws ConflictException, BadRequestException;

    UserEntity getByUserName(String userName);

    List<UserEntity> getAll();

    UserDetails loadUserByUsername(String userName) throws NotFoundException;

    boolean isUserWhoItClaimsToBe(String username, HttpServletRequest req);

    UserEntity updateUser(UserEntity userEntity);
}

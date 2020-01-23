package iths.theroom.service;

import iths.theroom.entity.UserEntity;
import iths.theroom.exception.NotFoundException;
import iths.theroom.repository.UserRepository;
import iths.theroom.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public UserPrincipalDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<UserEntity> user = this.userRepository.findByUserName(userName);
        user.orElseThrow(() -> new NotFoundException("User " + userName + " not found!"));

        return new UserPrincipal(user.get());
    }
}

package iths.theroom.repository;

import iths.theroom.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserName(String userName);
    Optional<UserEntity> findByEmail(String email);
    @Query(value = "SELECT * FROM user WHERE user_name = :username", nativeQuery = true)
    UserEntity findUserByNameWithQuery(@Param("username") String username);
}

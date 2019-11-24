package iths.theroom.dao;

import iths.theroom.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<UserEntity> {

    @Override
    public Optional<UserEntity> findOne(Long id) {
        return Optional.empty();
    }

    @Override
    public List<UserEntity> getAll() {
        return null;
    }

    @Override
    public void save(UserEntity userEntity) {

    }

    @Override
    public void update(UserEntity userEntity) {

    }

    @Override
    public void delete(UserEntity userEntity) {

    }
}

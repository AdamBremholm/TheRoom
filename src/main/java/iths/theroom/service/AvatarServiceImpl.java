package iths.theroom.service;

import iths.theroom.entity.AvatarEntity;
import iths.theroom.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class AvatarServiceImpl implements AvatarService {

    @Autowired
    private AvatarRepository repository;

    @Override
    public List<AvatarEntity> getAllAvatars() {
        return repository.findAll();
    }

    @Override
    public Optional<AvatarEntity> getAvatarById(Long id) {
        return repository.findById(id);
    }
}

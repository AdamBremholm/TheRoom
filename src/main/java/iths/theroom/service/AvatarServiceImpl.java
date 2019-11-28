package iths.theroom.service;

import iths.theroom.entity.AvatarEntity;
import iths.theroom.model.AvatarModel;
import iths.theroom.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import static iths.theroom.factory.AvatarFactory.*;

@Service
public class AvatarServiceImpl implements AvatarService {

    @Autowired
    private AvatarRepository repository;

    @Override
    public List<AvatarModel> getAllAvatars() {
        List<AvatarEntity> entities = repository.findAll();
        return entitiesToModel(entities);
    }

    @Override
    public AvatarModel getAvatarById(Long id) {
        Optional<AvatarEntity> entity = repository.findById(id);
        return entityToModel(entity.get());
    }

    @Override
    public AvatarModel createAvatar(AvatarEntity avatar) {
        AvatarEntity entity = repository.save(avatar);
        return entityToModel(entity);
    }

    @Override
    public AvatarModel updateAvatar(AvatarEntity avatar) {
        return entityToModel(avatar);
    }

    @Override
    public void deleteAvatar(Long id) {
        repository.deleteById(id);
    }
}

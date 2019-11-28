package iths.theroom.service;

import iths.theroom.entity.AvatarEntity;
import iths.theroom.factory.EntityFactory;
import iths.theroom.model.AvatarModel;
import iths.theroom.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AvatarServiceImpl implements AvatarService {

    @Autowired
    private AvatarRepository repository;
    @Autowired
    private EntityFactory factory;

    @Override
    public List<AvatarModel> getAllAvatars() {
        List<AvatarEntity> entities = repository.findAll();
        return factory.entityToModel(entities);
    }

    @Override
    public Optional<AvatarModel> getAvatarById(Long id) {
        Optional<AvatarEntity> entity = repository.findById(id);
        return (Optional<AvatarModel>) factory.entityToModel(entity);
    }

    @Override
    public AvatarModel createAvatar(AvatarEntity avatar) {
        AvatarEntity entity = repository.save(avatar);
        return (AvatarModel) factory.entityToModel(entity);
    }

    @Override
    public AvatarModel updateAvatar(AvatarEntity avatar) {
        return (AvatarModel) factory.entityToModel(avatar);
    }

    @Override
    public AvatarModel deleteAvatar(Long id) {
        repository.deleteById(id);
        return (AvatarModel) factory.entityToModel(repository.findById(id));
    }
}

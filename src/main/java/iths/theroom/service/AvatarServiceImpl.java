package iths.theroom.service;

import iths.theroom.entity.AvatarEntity;
import iths.theroom.exception.NotFoundException;
import iths.theroom.factory.AvatarFactory;
import iths.theroom.model.AvatarModel;
import iths.theroom.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import static iths.theroom.factory.AvatarFactory.*;

@Service
public class AvatarServiceImpl implements AvatarService {

    private final AvatarRepository repository;

    @Autowired
    public AvatarServiceImpl(AvatarRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AvatarModel> getAllAvatars() {
        List<AvatarEntity> entities = repository.findAll();
        return entitiesToModel(entities);
    }

    @Override
    public AvatarModel getAvatarById(Long id) {
        Optional<AvatarEntity> entity = repository.findById(id);
        return entityToModel(entity.orElseThrow(() -> new NotFoundException("avatar not found")));
    }

    @Override
    public AvatarModel createAvatar(AvatarEntity avatar) {
        AvatarEntity entity = repository.save(avatar);
        return entityToModel(entity);
    }

    @Override
    public AvatarModel findByUuid(String uuid){
        Optional<AvatarEntity> optionalAvatarEntity = repository.findByUuid(uuid);
        optionalAvatarEntity.orElseThrow(() -> new NotFoundException("avatar not found"));
        return AvatarFactory.entityToModel(optionalAvatarEntity.get());
    }

    @Override
    public AvatarModel updateAvatar(String id, AvatarEntity avatar) {
        Optional<AvatarEntity> optionalAvatarEntity = repository.findById(avatar.getId());
        optionalAvatarEntity.orElseThrow(() -> new NotFoundException("avatar not found"));
        if(avatar.getTorso() != 0) optionalAvatarEntity.get().setTorso(avatar.getTorso());
        if(avatar.getBase() != 0) optionalAvatarEntity.get().setBase(avatar.getBase());
        if(avatar.getHead() != 0) optionalAvatarEntity.get().setHead(avatar.getHead());
        if(avatar.getLegs() != 0) optionalAvatarEntity.get().setLegs(avatar.getLegs());
        AvatarEntity result = repository.save(optionalAvatarEntity.get());
        return entityToModel(result);
    }

    @Override
    public void deleteAvatar(String id) {
        Optional<AvatarEntity> optionalAvatarEntity = repository.findByUuid(id);
        optionalAvatarEntity.orElseThrow(() -> new NotFoundException("avatar not found"));
        repository.deleteById(optionalAvatarEntity.get().getId());
    }
}

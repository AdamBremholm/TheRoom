package iths.theroom.service;

import iths.theroom.entity.AvatarEntity;
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

    @Override
    public List<AvatarModel> getAllAvatars() {
        return null;
    }

    @Override
    public Optional<AvatarModel> getAvatarById(Long id) {
        return null;
    }

    @Override
    public AvatarModel saveAvatar(AvatarEntity avatar) {
        return null;
    }

    @Override
    public AvatarModel updateAvatar(AvatarEntity avatar) {
        return null;
    }

    @Override
    public AvatarModel deleteAvatar(AvatarEntity avatar) {
        return null;
    }
}

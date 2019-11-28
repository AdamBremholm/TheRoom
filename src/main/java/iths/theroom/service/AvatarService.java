package iths.theroom.service;

import iths.theroom.entity.AvatarEntity;

import java.util.List;
import java.util.Optional;

public interface AvatarService {

    List<AvatarEntity> getAllAvatars();
    Optional<AvatarEntity> getAvatarById(Long id);
}

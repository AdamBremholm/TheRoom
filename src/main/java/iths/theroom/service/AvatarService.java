package iths.theroom.service;

import iths.theroom.entity.AvatarEntity;

import java.util.List;

public interface AvatarService {

    List<AvatarEntity> getAllAvatars();
    AvatarEntity getAvatarById(Long id);
}

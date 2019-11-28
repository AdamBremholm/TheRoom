package iths.theroom.service;

import iths.theroom.entity.AvatarEntity;
import iths.theroom.model.AvatarModel;

import java.util.List;
import java.util.Optional;

public interface AvatarService {

    List<AvatarModel> getAllAvatars();
    Optional<AvatarModel> getAvatarById(Long id);
    AvatarModel saveAvatar(AvatarEntity avatar);
    AvatarModel updateAvatar(AvatarEntity avatar);
    AvatarModel deleteAvatar(AvatarEntity avatar);

}

package iths.theroom.service;

import iths.theroom.entity.AvatarEntity;
import iths.theroom.model.AvatarModel;

import java.util.List;
import java.util.Optional;

public interface AvatarService {

    List<AvatarModel> getAllAvatars();
    AvatarModel getAvatarById(Long id);
    AvatarModel createAvatar(AvatarEntity avatar);
    AvatarModel updateAvatar(AvatarEntity avatar);
    void deleteAvatar(Long id);

}

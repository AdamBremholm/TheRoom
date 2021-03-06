package iths.theroom.service;

import iths.theroom.entity.AvatarEntity;
import iths.theroom.model.AvatarModel;

import java.util.List;

public interface AvatarService {

    List<AvatarModel> getAllAvatars();

    AvatarModel getAvatarById(Long id);

    AvatarModel findByUuid(String uuid);

    AvatarModel createAvatar(AvatarEntity avatar);

    AvatarModel updateAvatar(String id, AvatarEntity avatar);

    void deleteAvatar(String id);

}

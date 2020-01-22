package iths.theroom.factory;

import iths.theroom.entity.AvatarEntity;
import iths.theroom.model.AvatarModel;

import java.util.ArrayList;
import java.util.List;

public class AvatarFactory {

    public static AvatarModel entityToModel(AvatarEntity avatarEntity) {
        AvatarModel model = new AvatarModel();
        model.setBase(avatarEntity.getBase());
        model.setHead(avatarEntity.getHead());
        model.setTorso(avatarEntity.getTorso());
        model.setLegs(avatarEntity.getLegs());
        model.setUuid(avatarEntity.getUuid());

        return model;
    }

    public static List<AvatarModel> entitiesToModel(List<AvatarEntity> avatarEntities) {
        List<AvatarModel> avatarModels = new ArrayList<>();
        if(avatarEntities != null) {
            avatarEntities.forEach(avatarEntity ->
                    avatarModels.add(entityToModel(avatarEntity)));
        }
        return avatarModels;
    }

}

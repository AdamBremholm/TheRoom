package iths.theroom.factory;

import iths.theroom.entity.AvatarEntity;
import iths.theroom.model.AvatarModel;

import java.util.ArrayList;
import java.util.List;

public class AvatarFactory implements EntityFactory<AvatarModel, AvatarEntity> {

    @Override
    public AvatarModel entityToModel(AvatarEntity avatarEntity) {
        AvatarModel model = new AvatarModel();
        model.setBase(avatarEntity.getBase());
        model.setHead(avatarEntity.getHead());
        model.setTorso(avatarEntity.getTorso());
        model.setLegs(avatarEntity.getLegs());

        return model;
    }

    @Override
    public List<AvatarModel> entityToModel(List<AvatarEntity> avatarEntities) {
        List<AvatarModel> avatarModels = new ArrayList<>();
        if(avatarEntities != null) {
            avatarEntities.forEach(avatarEntity ->
                    avatarModels.add(entityToModel(avatarEntity)));
        }
        return avatarModels;
    }

   /* public AvatarModel toModel(AvatarEntity avatar) {
        AvatarModel model = new AvatarModel();
        model.setBase(avatar.getBase());
        model.setHead(avatar.getHead());
        model.setTorso(avatar.getTorso());
        model.setLegs(avatar.getLegs());

        return model;
    }*/
}

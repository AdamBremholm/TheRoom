package iths.theroom.factory;

import iths.theroom.entity.AvatarEntity;
import iths.theroom.model.AvatarModel;

public class AvatarFactory {

    public AvatarModel toModel(AvatarEntity avatar) {
        AvatarModel model = new AvatarModel();
        model.setBase(avatar.getBase());
        model.setHead(avatar.getHead());
        model.setTorso(avatar.getTorso());
        model.setLegs(avatar.getLegs());

        return model;
    }
}

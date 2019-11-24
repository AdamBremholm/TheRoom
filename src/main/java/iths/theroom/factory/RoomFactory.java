package iths.theroom.factory;

import iths.theroom.entity.Room;
import iths.theroom.model.RoomModel;

public class RoomFactory {
public static RoomModel toModel(Room room, Role role) {
    //The role class contains the "Access level" which will produce different models.
    //Think of it like admins will see more fleshed out models whilst a guest might have a really limited view.
    return new RoomModel(room, role);
}
}

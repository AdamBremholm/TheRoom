package iths.theroom.factory;

import iths.theroom.entity.RoleEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.model.RoleModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoleFactory {

    public static RoleModel toModel(RoleEntity roleEntity){
        RoleModel roleModel = new RoleModel();
        if(roleEntity!=null){
           roleModel.setRole(roleEntity.getRole());
           roleModel.setUsers(roleEntity.getUsers().stream().map(UserEntity::getUserName).collect(Collectors.toSet()));
        }

        return roleModel;

    }
    public static List<RoleModel> toModel(List<RoleEntity> roleEntities){
        List<RoleModel> roleModels = new ArrayList<>();
        if(roleEntities!=null){
            roleEntities.forEach(r -> roleModels.add(toModel(r)));
        }
        return roleModels;
    }
}

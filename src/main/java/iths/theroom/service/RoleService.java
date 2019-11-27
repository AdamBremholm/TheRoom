package iths.theroom.service;

import iths.theroom.entity.RoleEntity;
import java.util.List;


public interface RoleService {

    List<RoleEntity> getAllRoles();
    List<RoleEntity> getRoles(List<RoleEntity.Role> roles);
    RoleEntity save(RoleEntity roleEntity);
}

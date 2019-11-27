package iths.theroom.model;

import iths.theroom.entity.RoleEntity;

import java.util.HashSet;
import java.util.Set;


public class RoleModel {

    private RoleEntity.Role role;
    private Set<String> users;

    public RoleModel() {
        this.users = new HashSet<>();
    }

    public RoleModel(RoleEntity.Role role, Set<String> users) {
        this.role = role;
        this.users = users;
    }

    public RoleEntity.Role getRole() {
        return role;
    }

    public void setRole(RoleEntity.Role role) {
        this.role = role;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }


}

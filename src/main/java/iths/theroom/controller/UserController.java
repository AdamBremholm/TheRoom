package iths.theroom.controller;


import iths.theroom.entity.RoleEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.model.UserModel;
import iths.theroom.service.RoleService;
import iths.theroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static iths.theroom.factory.UserFactory.*;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public List<UserModel> getAllMessages() {
        return toModel(userService.getAll());
    }

    @PostMapping("/users")
    public UserModel addMessage(@RequestBody UserEntity userEntity) {
        List<RoleEntity> matchingRoleEntities = getMatchingRoleEntitiesFromDb(userEntity);
        userEntity.getRoles().clear();
        userEntity.getRoles().addAll(matchingRoleEntities);
        return toModel(userService.save(userEntity));
    }

    private List<RoleEntity> getMatchingRoleEntitiesFromDb(@RequestBody UserEntity userEntity) {
       List<RoleEntity.Role> formatOkRoles = userEntity.getRoleList().stream().filter(RoleEntity::existsInRole)
               .map(String::toUpperCase)
               .map(RoleEntity.Role::valueOf)
               .collect(Collectors.toList());
        return roleService.getRoles(formatOkRoles);
    }



}

package iths.theroom.controller;

import iths.theroom.entity.RoleEntity;
import iths.theroom.model.RoleModel;
import iths.theroom.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static iths.theroom.factory.RoleFactory.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    RoleService roleService;


    @GetMapping("/roles")
    public List<RoleModel> getAllMessages() {
        return toModel(roleService.getAllRoles());

    }

    @PostMapping("/roles")
    public RoleModel addRole(@RequestBody RoleEntity roleEntity) {
      return toModel(roleService.save(roleEntity));
    }


}

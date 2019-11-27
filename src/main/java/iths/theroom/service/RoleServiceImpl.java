package iths.theroom.service;

import iths.theroom.entity.RoleEntity;
import iths.theroom.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{


    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<RoleEntity> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<RoleEntity> getRoles(List<RoleEntity.Role> roles) {
       return roleRepository.findByRoles(roles);
    }

    @Override
    public RoleEntity save(RoleEntity roleEntity) {
        return roleRepository.save(roleEntity);
    }
}

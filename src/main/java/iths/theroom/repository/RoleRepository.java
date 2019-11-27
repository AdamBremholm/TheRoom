package iths.theroom.repository;

import iths.theroom.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByRole(RoleEntity.Role role);

    @Query("SELECT roleEntity FROM RoleEntity roleEntity WHERE roleEntity.role IN :roles")
    List<RoleEntity> findByRoles(List<RoleEntity.Role> roles);

}

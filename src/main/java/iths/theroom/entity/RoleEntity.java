package iths.theroom.entity;

import iths.theroom.config.Role;

import javax.persistence.*;

@Entity
@Table(name="TABLE-ROLE")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    public RoleEntity() {
    }

    public RoleEntity(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

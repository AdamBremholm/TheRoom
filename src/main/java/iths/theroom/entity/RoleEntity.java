package iths.theroom.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<UserEntity> users;

    @ManyToMany
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Collection<PrivilegeEntity> privileges;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<UserEntity> getUsers() {
        return users;
    }

    public Collection<PrivilegeEntity> getPrivileges() {
        return privileges;
    }
}
package iths.theroom.entity;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import static iths.theroom.config.DataBaseConfig.*;

@Entity
@Table(name=TABLE_ROLE)
public class RoleEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Role role;

    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<UserEntity> users;

    public RoleEntity() {
     this.users = new HashSet<>();
    }

    public RoleEntity(Role role) {
        this.role = role;
        this.users = new HashSet<>();
    }


    public RoleEntity(Role role, Set<UserEntity> users) {
        this.role = role;
        this.users = users;
    }

    public Role getRole() {
        return role;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    public enum Role {
        GLOBAL_ADMIN,
        MODERATOR,
        USER,
        GUEST,
    }
}

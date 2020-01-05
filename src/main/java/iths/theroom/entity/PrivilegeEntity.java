package iths.theroom.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class PrivilegeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<RoleEntity> roles;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<RoleEntity> getRoles() {
        return roles;
    }
}

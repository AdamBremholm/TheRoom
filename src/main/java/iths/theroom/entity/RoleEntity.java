package iths.theroom.entity;

import javax.persistence.*;

@Entity
@Table(name="TABLE_ROLE")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="role")
    private String role;

    public RoleEntity() {
    }

    public RoleEntity(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}

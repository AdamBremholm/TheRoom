package iths.theroom.entity;

import javax.persistence.*;

@Entity
@Table(name="TABLE_ROLE")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String role;

    public RoleEntity() {
    }

    public RoleEntity(String role) {
        this.role = role;
    }


}

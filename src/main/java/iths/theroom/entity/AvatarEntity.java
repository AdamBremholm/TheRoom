package iths.theroom.entity;

import javax.persistence.*;

import static iths.theroom.config.DataBaseConfig.COLUMN_USER_ID;
import static iths.theroom.config.DataBaseConfig.TABLE_AVATAR;

@Entity
@Table(name=TABLE_AVATAR)
public class AvatarEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private int base;
    private int head;
    private int torso;
    private int legs;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = COLUMN_USER_ID)
    private UserEntity userEntity;

    public AvatarEntity() {
    }

    public AvatarEntity(int base, int head, int torso, int legs) {
        this.base = base;
        this.head = head;
        this.torso = torso;
        this.legs = legs;
    }

    public long getId() {
        return id;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public int getTorso() {
        return torso;
    }

    public void setTorso(int torso) {
        this.torso = torso;
    }

    public int getLegs() {
        return legs;
    }

    public void setLegs(int legs) {
        this.legs = legs;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}

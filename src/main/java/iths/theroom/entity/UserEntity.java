package iths.theroom.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import static iths.theroom.config.DataBaseConfig.*;


@Entity(name= TABLE_USER)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userName;
    private String password;
    private String email;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
    @JoinTable(name = JOIN_TABLE_USER_ROLE,
            joinColumns=@JoinColumn(name=COLUMN_ROLE_ID, referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = COLUMN_USER_ID, referencedColumnName = "id"))
    private Set<RoleEntity> roles;

    @Transient
    private String passwordConfirm;
    @Transient
    private List<String> roleList;

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "sender")
    private Set<MessageEntity> messages;


    public UserEntity(String userName, String password, String email, Set<RoleEntity> roles,
                      String passwordConfirm, String firstName, String lastName, Set<MessageEntity> messages, List<String> roleList) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.roles = Objects.requireNonNullElse(roles, new HashSet<>());
        this.passwordConfirm = passwordConfirm;
        this.firstName = firstName;
        this.lastName = lastName;
        this.messages = Objects.requireNonNullElse(messages, new HashSet<>());
        this.roleList = roleList;
    }

    public UserEntity() {
        this(null, null, null, null, null, null, null, null, null);
    }

    public UserEntity(String userName) {
        this(userName, null, null, null, null, null, null, null, null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    public Set<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(Set<MessageEntity> messages) {
        this.messages = messages;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }
}

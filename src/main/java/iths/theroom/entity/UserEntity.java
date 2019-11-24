package iths.theroom.entity;

import javax.persistence.*;
import java.util.Set;

import static iths.theroom.config.DataBaseConfig.*;

@Entity
@Table(name=TABLE_USER)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String password;
    private String email;

    @Transient
    private String passwordConfirm;

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "sender")
<<<<<<< HEAD
    private Set<MessageEntity> messageEntities;
=======
    private Set<MessageEntity> messages;
>>>>>>> d927a0b98ff829cd04b8c5413b9df02c313b9ba7

    public UserEntity() {
    }

    public UserEntity(String userName) {
        this.userName = userName;
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

<<<<<<< HEAD
    public Set<MessageEntity> getMessageEntities() {
        return messageEntities;
    }

    public void setMessageEntities(Set<MessageEntity> messageEntities) {
        this.messageEntities = messageEntities;
=======
    public Set<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(Set<MessageEntity> messages) {
        this.messages = messages;
>>>>>>> d927a0b98ff829cd04b8c5413b9df02c313b9ba7
    }
}

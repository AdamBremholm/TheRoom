package iths.theroom.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class UserModel implements Serializable {

    private String userName;
    private String email;

    private String firstName;
    private String lastName;

    private Set<MessageModel> messages;
    private Set<String> roles;

    public UserModel() {
        this.messages = new HashSet<>();
        this.roles = new HashSet<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Set<MessageModel> getMessages() {
        return messages;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}

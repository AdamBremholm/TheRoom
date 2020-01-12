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
    private AvatarModel avatar;
    private String roles;
    private String permissions;
    private ProfileModel profileModel;

    public UserModel() {
        this.messages = new HashSet<>();
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

    public AvatarModel getAvatar() {
        return avatar;
    }

    public void setAvatar(AvatarModel avatar) {
        this.avatar = avatar;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public ProfileModel getProfileModel() { return profileModel; }

    public void setProfileModel(ProfileModel profileModel) { this.profileModel = profileModel; }
}

package iths.theroom.entity;

import javax.persistence.*;
import java.util.*;

import static iths.theroom.config.DataBaseConfig.*;


@Entity(name= TABLE_USER)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String userName;
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    @Transient
    private String passwordConfirm;
    private String firstName;
    private String lastName;
    private String roles;
    private String permissions;
    private int active;

    @OneToMany(mappedBy = "sender")
    private Set<MessageEntity> messages;

    @OneToOne(mappedBy = "userEntity", cascade = {CascadeType.PERSIST, CascadeType.PERSIST})
    @JoinColumn(name = COLUMN_ID, referencedColumnName = COLUMN_ID)
    private AvatarEntity avatarEntity;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)
    @JoinColumn(name=COLUMN_BANNED_ID, referencedColumnName = COLUMN_ID)
    private Set<RoomEntity> excludedRooms;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.PERSIST}, fetch=FetchType.LAZY)
    @JoinColumn(name = COLUMN_PROFILE_ID, referencedColumnName = COLUMN_ID)
    private ProfileEntity profile;

    public UserEntity(String userName, String password, String email,
                      String passwordConfirm, String firstName, String lastName, Set<MessageEntity> messages, AvatarEntity avatarEntity) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.passwordConfirm = passwordConfirm;
        this.firstName = firstName;
        this.lastName = lastName;
        this.messages = Objects.requireNonNullElse(messages, new HashSet<>());
        this.avatarEntity = avatarEntity;
        this.excludedRooms = new HashSet<>();
        this.active = 1;
        this.profile = new ProfileEntity();
        profile.setGender("1");
        this.roles = "USER";
        this.permissions = "";
    }

    public UserEntity() {
        this(null, null, null, null, null, null, null, null);
    }

    public UserEntity(String userName) {
        this(userName, null, null, null, null, null, null, null);
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

    public Set<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(Set<MessageEntity> messages) {
        this.messages = messages;
    }

    public Set<RoomEntity> getExcludedRooms() {
        return excludedRooms;
    }

    public void setExcludedRooms(Set<RoomEntity> excludedRooms) {
        this.excludedRooms = excludedRooms;
    }

    public AvatarEntity getAvatarEntity() {
        return avatarEntity;
    }

    public void setAvatarEntity(AvatarEntity avatarEntity) {
        this.avatarEntity = avatarEntity;
    }

    public void blackListInRoom(RoomEntity room){
        this.excludedRooms.add(room);
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

    public int getActive() {
        return active;
    }

    public List<String> getRolesAsList(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    public List<String> getPermissionsAsList(){
        if(this.permissions.length() > 0){
            return Arrays.asList(this.permissions.split(","));
        }
        return new ArrayList<>();
    }

    public ProfileEntity getProfile() { return profile; }

    public void setProfile(ProfileEntity profile) { this.profile = profile; }

}

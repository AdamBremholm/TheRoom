package iths.theroom.entity;

import javax.persistence.*;


@Entity(name= "profile")
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int gender;
    private String country;
    private int age;
    private String aboutMe;
    private String starSign;
    private long visitors;

public ProfileEntity(){

    }

    @OneToOne(mappedBy = "profile")
    private UserEntity profileOwner;


    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public UserEntity getProfileOwner() {
        return profileOwner;
    }

    public void setProfileOwner(UserEntity profileOwner) {
        this.profileOwner = profileOwner;
    }

    public String getStarSign() {
        return starSign;
    }

    public void setStarSign(String starSign) {
        this.starSign = starSign;
    }

    public long getVisitors() {
        return visitors;
    }

    public void setVisitors(long visitors) {
        this.visitors = visitors;
    }
}

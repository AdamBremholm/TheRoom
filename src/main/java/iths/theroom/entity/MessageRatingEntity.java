package iths.theroom.entity;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static iths.theroom.config.DataBaseConfig.*;

@Entity
@Table(name= TABLE_MESSAGE_RATING)
public class MessageRatingEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private int rating;

    @OneToOne(mappedBy = "messageRatingEntity")
    private MessageEntity messageEntity;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = TABLE_USER_INCREASE,
            joinColumns = {@JoinColumn(name = COLUMN_RATING_ID)},
            inverseJoinColumns = {@JoinColumn(name = COLUMN_USER_ID)})
    private Set<UserEntity> usersIncrease;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = TABLE_USER_DECREASE,
            joinColumns = {@JoinColumn(name = COLUMN_RATING_ID)},
            inverseJoinColumns = {@JoinColumn(name = COLUMN_USER_ID)})
    private Set<UserEntity> usersDecrease;

    public MessageRatingEntity(){
        usersIncrease = new HashSet<>();
        usersDecrease = new HashSet<>();
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void increaseRating(UserEntity userEntity){

        if(usersDecrease.contains(userEntity)){
            this.rating ++;
            usersDecrease.remove(userEntity);
        }
        else if(!usersIncrease.contains(userEntity)){
            usersIncrease.add(userEntity);
            this.rating ++;
        }
    }

    public void decreaseRating(UserEntity userEntity){

        if(usersIncrease.contains(userEntity)){
            this.rating --;
            usersIncrease.remove(userEntity);
        }
        else if(!usersDecrease.contains(userEntity)){
            usersDecrease.add(userEntity);
            this.rating --;
        }
    }
}

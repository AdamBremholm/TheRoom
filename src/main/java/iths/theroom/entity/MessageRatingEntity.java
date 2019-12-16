package iths.theroom.entity;

import javax.persistence.*;

import static iths.theroom.config.DataBaseConfig.TABLE_MESSAGE_RATING;

@Entity
@Table(name= TABLE_MESSAGE_RATING)
public class MessageRatingEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private int rating;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private MessageEntity messageEntity;

    public MessageRatingEntity(){}

    public MessageRatingEntity(int rating){
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void increaseRating(){
        this.rating ++;
    }

    public void decreaseRating(){
        this.rating --;
    }
}

package iths.theroom.entity;

import javax.persistence.*;

import static iths.theroom.config.DataBaseConfig.TABLE_RATING;

@Entity
@Table(name=TABLE_RATING)
public class RatingEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private int rating;

    public RatingEntity(){}

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

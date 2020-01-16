package iths.theroom.entity;

import iths.theroom.enums.Type;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static iths.theroom.config.DataBaseConfig.*;

@Entity
@Table(name = TABLE_MESSAGE)
public class MessageEntity {

    @Id
    @GeneratedValue
    private long id;
    @Column(name = "uuid", unique = true)
    private String uuid;
    private Type type;
    private String content;
    private Instant time;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
    @JoinColumn(name=COLUMN_USER_ID)
    private UserEntity sender;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.PERSIST}, fetch=FetchType.LAZY)
    @JoinColumn(name=COLUMN_ROOM_ID)
    private RoomEntity roomEntity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rating_id", referencedColumnName = "id")
    private MessageRatingEntity messageRatingEntity;

    public MessageEntity() {
        this.sender = new UserEntity("guest");
        this.roomEntity = new RoomEntity("unspecified");
        this.messageRatingEntity = new MessageRatingEntity();
        this.time = Instant.now();
    }

    public MessageEntity(Type type, String content, UserEntity sender, RoomEntity roomEntity, Instant time, MessageRatingEntity rating) {
        this.type = Objects.requireNonNullElse(type, Type.UNDEFINED);
        this.content = Objects.requireNonNullElse(content, "");
        this.sender = Objects.requireNonNullElse(sender, new UserEntity("guest"));
        this.roomEntity = Objects.requireNonNullElse(roomEntity, new RoomEntity("unspecified"));
        this.time = Objects.requireNonNullElse(time, Instant.now());
        this.messageRatingEntity = Objects.requireNonNullElse(rating, new MessageRatingEntity());
    }

    public MessageEntity(Type type, String content, UserEntity sender, RoomEntity roomEntity, MessageRatingEntity rating) {
        this(type, content, sender, roomEntity, Instant.now(), rating);

    }

    public long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        if(this.uuid==null)
            this.uuid = uuid;
        else
            throw new UnsupportedOperationException("set uuid only allowed once");
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public RoomEntity getRoomEntity() {
        return roomEntity;
    }

    public void setRoomEntity(RoomEntity roomEntity) {
        this.roomEntity = roomEntity;

    }

    public MessageRatingEntity getMessageRatingEntity() {
        return messageRatingEntity;
    }

    public void setMessageRatingEntity(MessageRatingEntity messageRatingEntity) {
        this.messageRatingEntity = messageRatingEntity;
    }

    @PrePersist
    public void initializeUUID() {
        if (getUuid() == null) {
            setUuid(UUID.randomUUID().toString());
        }
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", sender=" + sender +
                ", roomEntity=" + roomEntity +
                ", time=" + time +
                '}';
    }
}

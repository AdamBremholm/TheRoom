package iths.theroom.entity;

import static iths.theroom.config.DataBaseConfig.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = TABLE_MESSAGE)
public class MessageEntity {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "uuid", unique = true)
    private String uuid;
    private Type type;
    private String content;
    private Instant time;
    private long upVotes;
    private long downVotes;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
    @JoinColumn(name=COLUMN_USER_ID)
    private UserEntity sender;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
    @JoinColumn(name=COLUMN_ROOM_ID)
    private RoomEntity room;

    public MessageEntity() {
        this.sender = new UserEntity("guest");
        this.room = new RoomEntity("unspecified");
        this.time = Instant.now();
    }

    public MessageEntity(Type type, String content, UserEntity sender, RoomEntity room, Instant time, long upVotes, long downVotes) {
        this.type = Objects.requireNonNullElse(type, Type.UNDEFINED);
        this.content = Objects.requireNonNullElse(content, "");
        this.sender = Objects.requireNonNullElse(sender, new UserEntity("guest"));
        this.room = Objects.requireNonNullElse(room, new RoomEntity("unspecified"));
        this.time = Objects.requireNonNullElse(time, Instant.now());
        this.upVotes = upVotes;
        this.downVotes = downVotes;
    }

    public MessageEntity(Type type, String content, UserEntity sender, RoomEntity room, Instant time) {
       this(type, content, sender, room, time, 0L, 0L);
    }

    public MessageEntity(Type type, String content, UserEntity sender, RoomEntity room) {
        this(type, content, sender, room, Instant.now(), 0L, 0L);
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

    public long getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(long upVotes) {
        this.upVotes = upVotes;
    }

    public long getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(long downVotes) {
        this.downVotes = downVotes;
    }

    public enum Type {
        CHAT,
        PM,
        UNDEFINED
    }

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }

    @PrePersist
    public void initializeUUID() {
        if (getUuid() == null) {
            setUuid(UUID.randomUUID().toString());
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", sender=" + sender +
                ", room=" + room +
                ", time=" + time +
                ", upVotes=" + upVotes +
                ", downVotes=" + downVotes +
                '}';
    }
}
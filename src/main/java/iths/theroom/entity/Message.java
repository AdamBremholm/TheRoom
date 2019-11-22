package iths.theroom.entity;

import static iths.theroom.config.DataBaseConfig.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = TABLE_MESSAGE)
public class Message {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "uuid", unique = true)
    private String uuid;
    private Type type;
    private String content;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
    @JoinColumn(name=COLUMN_USER_ID)
    private User sender;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
    @JoinColumn(name=COLUMN_ROOM_ID)
    private Room room;
    private Instant time;
    private long upVotes;
    private long downVotes;

    public Message() {
        this.sender = new User("guest");
        this.room = new Room("unspecified");
        this.time = Instant.now();
    }

    public Message(Type type, String content, User sender, Room room, Instant time, long upVotes, long downVotes) {
        this.type = Objects.requireNonNullElse(type, Type.UNDEFINED);
        this.content = Objects.requireNonNullElse(content, "");
        this.sender = Objects.requireNonNullElse(sender, new User("guest"));
        this.room = Objects.requireNonNullElse(room, new Room("unspecified"));
        this.time = Objects.requireNonNullElse(time, Instant.now());
        this.upVotes = upVotes;
        this.downVotes = downVotes;
    }

    public Message(Type type, String content, User sender, Room room, Instant time) {
       this(type, content, sender, room, time, 0L, 0L);
    }

    public Message(Type type, String content, User sender, Room room) {
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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
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

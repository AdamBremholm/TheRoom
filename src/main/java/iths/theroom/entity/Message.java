package iths.theroom.entity;

import iths.theroom.config.DataBaseConfig;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = DataBaseConfig.TABLE_MESSAGE)
public class Message {

    @Id
    @GeneratedValue
    private Long id;
    private Type type;
    private String content;
    // Can change to User object or uuid when created
    private String sender;
    private Instant time;
    private long upVotes;
    private long downVotes;

    public Message() {
    }

    public Message(Type type, String content, String sender, Instant time, long upVotes, long downVotes) {
        this.type = Objects.requireNonNullElse(type, Type.UNDEFINED);
        this.content = Objects.requireNonNullElse(content, "");
        this.sender = Objects.requireNonNullElse(content, "unknown");
        this.time = Objects.requireNonNullElse(time, Instant.now());
        this.upVotes = upVotes;
        this.downVotes = downVotes;
    }

    public Message(Type type, String content, String sender, Instant time) {
       this(type, content, sender, time, 0L, 0L);
    }

    public Message(Type type, String content, String sender) {
        this(type, content, sender, Instant.now(), 0L, 0L);
    }


    public long getId() {
        return id;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
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

}

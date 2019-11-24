package iths.theroom.model;

public class MessageModel {

    private String uuid;
    private String type;
    private String content;
    private String sender;
    private String time;
    private String room;
    private long upVotes;
    private long downVotes;


    public MessageModel(String type, String content, String sender, String time, String room, long upVotes, long downVotes) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.time = time;
        this.room = room;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
    }

    public MessageModel() {
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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


}

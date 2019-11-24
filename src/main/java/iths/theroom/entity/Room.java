package iths.theroom.entity;

import javax.persistence.*;
import java.util.HashSet;

@Entity(name="room")
public class Room {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="ROOM_NAME", length=50, nullable=false, unique=true)
    private String roomName;

    @OneToMany(mappedBy = "room")
    @Column(name="ROOM_MESSAGES", unique=false)
    private Set<Message> messages;

    public Room(){
        this.messages = new HashSet<>();
    }

    public Room(String roomName){
        this.roomName = roomName;
        this.messages = new HashSet<>();
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public addMessage(Message message){
        //Called when posting a message
        this.messages.add(message);
    }

    public removeMessage(Message message){
       //Called when deleting a users message
        this.messages.remove(message);
    }
}

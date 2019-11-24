package iths.theroom.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name="room")
public class RoomEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="ROOM_NAME", length=50, nullable=false, unique=true)
    private String roomName;

    @OneToMany(mappedBy = "room")
    @Column(name="ROOM_MESSAGES", unique=false)
    private Set<MessageEntity> messageEntities;

    public RoomEntity(){
        this.messageEntities = new HashSet<>();
    }

    public RoomEntity(String roomName){
        this.roomName = roomName;
        this.messageEntities = new HashSet<>();
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Set<MessageEntity> getMessageEntities() {
        return messageEntities;
    }

    public void setMessageEntities(Set<MessageEntity> messageEntities) {
        this.messageEntities = messageEntities;
    }

    public void addMessage(MessageEntity messageEntity){
        //Called when posting a messageEntity
        this.messageEntities.add(messageEntity);
    }

    public void removeMessage(MessageEntity messageEntity){
       //Called when deleting a users messageEntity
        this.messageEntities.remove(messageEntity);
    }
}

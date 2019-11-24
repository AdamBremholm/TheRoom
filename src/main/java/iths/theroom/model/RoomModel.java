package iths.theroom.model;

import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoomEntity;


import java.util.Set;

public class RoomModel {
    private String roomName;
    private Set<MessageEntity> messages;

    public RoomModel(){}
   // public RoomModel(RoomEntity room, Role role){}

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Set<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(Set<MessageEntity> messages) {
        this.messages = messages;
    }
}

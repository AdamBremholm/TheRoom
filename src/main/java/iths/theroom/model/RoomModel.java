package iths.theroom.model;

import iths.theroom.entity.Message;
import iths.theroom.entity.RoomEntity;


import java.util.Set;

public class RoomModel {
    private String roomName;
    private Set<Message> messages;

    public RoomModel(){}
    public RoomModel(RoomEntity room, Role role){}

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
}

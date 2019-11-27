package iths.theroom.model;

import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoleEntity;
import iths.theroom.entity.RoomEntity;


import java.util.Set;

public class RoomModel {

    private String roomName;
    private Set<MessageModel> messages;

    public RoomModel(){}

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Set<MessageModel> getMessages() {
        return messages;
    }

    public void setMessages(Set<MessageModel> messages) {
        this.messages = messages;
    }
}

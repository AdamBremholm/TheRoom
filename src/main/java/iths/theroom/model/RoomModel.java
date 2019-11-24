package iths.theroom.model;

import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoomEntity;


import java.util.Set;

public class RoomModel {
    private String roomName;

    private Set<MessageEntity> messageEntities;

    public RoomModel(){}
    public RoomModel(RoomEntity roomEntity, Role role){}

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
}

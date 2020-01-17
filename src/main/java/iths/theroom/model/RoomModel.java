package iths.theroom.model;

import java.util.HashSet;
import java.util.Set;

public class RoomModel {

    private String roomName;
    private String backgroundColor;
    private Set<MessageModel> messages;

    public RoomModel(){
        this.messages = new HashSet<>();
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Set<MessageModel> getMessages() {
        return messages;
    }

    public void addMessage(MessageModel message) {
        this.messages.add(message);
    }

    public void setMessages(Set<MessageModel> messages) {
        this.messages = messages;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}

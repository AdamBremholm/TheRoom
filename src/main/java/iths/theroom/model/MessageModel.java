package iths.theroom.model;

import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MessageModel {

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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getType() {
        return type;
    }

    public void setType(String string) {
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

    public static MessageModel toModel(MessageEntity message){
        MessageModel messageModel = new MessageModel();
        if(message!=null) {
            Optional.of(message)
                    .map(MessageEntity::getContent)
                    .filter(Predicate.not(String::isBlank))
                    .ifPresent(messageModel::setContent);
            Optional.of(message).map(MessageEntity::getSender)
                    .map(UserEntity::getUserName)
                    .filter(Predicate.not(String::isBlank))
                    .ifPresent(messageModel::setSender);
            Optional.of(message).map(MessageEntity::getRoom)
                    .map(RoomEntity::getRoomName)
                    .filter(Predicate.not(String::isBlank))
                    .ifPresent(messageModel::setRoom);
            Optional.of(message).map(MessageEntity::getTime)
                    .map(i -> i.truncatedTo(ChronoUnit.SECONDS).toString())
                    .filter(Predicate.not(String::isBlank))
                    .ifPresent(messageModel::setTime);
            Optional.of(message).map(MessageEntity::getType)
                    .map(Enum::toString)
                    .filter(Predicate.not(String::isBlank))
                    .ifPresent(messageModel::setType);
            messageModel.setUpVotes(message.getUpVotes());
            messageModel.setDownVotes(message.getDownVotes());
        }
        return messageModel;
    }

    public static List<MessageModel> toModel(List<MessageEntity> messages){
        List<MessageModel> messageModels = new ArrayList<>();
        if(messages!=null){
            messages.forEach(m -> messageModels.add(MessageModel.toModel(m)));
        }
        return messageModels;

    }


}

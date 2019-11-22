package iths.theroom.model;

import iths.theroom.entity.Message;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MessageModel {

    private String type;
    private String content;
    private String sender;
    private String time;
    private long upVotes;
    private long downVotes;


    public MessageModel() {
    }

    public MessageModel(String type, String content, String sender, String time, long upVotes, long downVotes) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.time = time;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
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

    public static MessageModel toModel(Message message){
        MessageModel messageModel = new MessageModel();
        if(message!=null) {
            Optional.of(message).map(Message::getContent).filter(Predicate.not(String::isBlank)).ifPresent(messageModel::setContent);
            Optional.of(message).map(Message::getSender).filter(Predicate.not(String::isBlank)).ifPresent(messageModel::setSender);
            Optional.of(message).map(Message::getTime).map(Instant::toString).filter(Predicate.not(String::isBlank)).ifPresent(messageModel::setTime);
            Optional.of(message).map(Message::getType).map(Enum::toString).filter(Predicate.not(String::isBlank)).ifPresent(messageModel::setType);
            messageModel.setUpVotes(message.getUpVotes());
            messageModel.setDownVotes(message.getDownVotes());
        }
        return messageModel;
    }

    public static List<MessageModel> toModel(List<Message> messages){
        List<MessageModel> messageModels = new ArrayList<>();
        if(messages!=null){
            messages.forEach(m -> messageModels.add(MessageModel.toModel(m)));
        }
        return messageModels;

    }


}

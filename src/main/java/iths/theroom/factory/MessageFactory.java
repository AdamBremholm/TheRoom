package iths.theroom.factory;

import iths.theroom.entity.Message;
import iths.theroom.entity.Room;
import iths.theroom.entity.UserEntity;
import iths.theroom.model.MessageModel;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MessageFactory {


    public static MessageModel toModel(Message message){
        MessageModel messageModel = new MessageModel();
        Optional.of(message)
                .map(Message::getUuid)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(messageModel::setUuid);
        Optional.of(message)
                .map(Message::getContent)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(messageModel::setContent);
        Optional.of(message).map(Message::getSender)
                .map(UserEntity::getUserName)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(messageModel::setSender);
        Optional.of(message).map(Message::getRoom)
                .map(Room::getName)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(messageModel::setRoom);
        Optional.of(message).map(Message::getTime)
                .map(i -> i.truncatedTo(ChronoUnit.SECONDS).toString())
                .filter(Predicate.not(String::isBlank))
                .ifPresent(messageModel::setTime);
        messageModel.setType(message.getType().name());
        messageModel.setUpVotes(message.getUpVotes());
        messageModel.setDownVotes(message.getDownVotes());

        return messageModel;
    }

    public static List<MessageModel> toModel(List<Message> messages){
        List<MessageModel> messageModels = new ArrayList<>();
        if(messages!=null){
            messages.forEach(m -> messageModels.add(toModel(m)));
        }
        return messageModels;

    }
}

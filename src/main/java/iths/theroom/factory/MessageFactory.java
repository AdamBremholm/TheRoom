package iths.theroom.factory;

import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.model.MessageModel;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MessageFactory {


    public static MessageModel toModel(MessageEntity message){
        MessageModel messageModel = new MessageModel();
        Optional.of(message)
                .map(MessageEntity::getUuid)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(messageModel::setUuid);
        Optional.of(message)
                .map(MessageEntity::getContent)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(messageModel::setContent);
        Optional.of(message).map(MessageEntity::getSender)
                .map(UserEntity::getUserName)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(messageModel::setSender);
        Optional.of(message).map(MessageEntity::getRoomEntity)
                .map(RoomEntity::getRoomName)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(messageModel::setRoom);
        Optional.of(message).map(MessageEntity::getTime)
                .map(i -> i.truncatedTo(ChronoUnit.SECONDS).toString())
                .filter(Predicate.not(String::isBlank))
                .ifPresent(messageModel::setTime);
        messageModel.setType(message.getType().name());
        messageModel.setUpVotes(message.getUpVotes());
        messageModel.setDownVotes(message.getDownVotes());

        return messageModel;
    }

    public static List<MessageModel> toModel(List<MessageEntity> messages){
        List<MessageModel> messageModels = new ArrayList<>();
        if(messages!=null){
            messages.forEach(m -> messageModels.add(toModel(m)));
        }
        return messageModels;

    }
}

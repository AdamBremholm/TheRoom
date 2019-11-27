package iths.theroom.factory;

import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.model.MessageModel;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class MessageFactory {

    public MessageModel entityToModel(MessageEntity messageEntity){
        MessageModel messageModel = new MessageModel();

        Optional.of(messageEntity)
                .map(MessageEntity::getUuid)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(messageModel::setUuid);

        Optional.of(messageEntity)
                .map(MessageEntity::getContent)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(messageModel::setContent);

        Optional.of(messageEntity).map(MessageEntity::getSender)
                .map(UserEntity::getUserName)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(messageModel::setSender);

        Optional.of(messageEntity).map(MessageEntity::getRoomEntity)
                .map(RoomEntity::getRoomName)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(messageModel::setRoom);

        Optional.of(messageEntity).map(MessageEntity::getTime)
                .map(i -> i.truncatedTo(ChronoUnit.SECONDS).toString())
                .filter(Predicate.not(String::isBlank))
                .ifPresent(messageModel::setTime);

        messageModel.setType(messageEntity.getType().name());
        messageModel.setUpVotes(messageEntity.getUpVotes());
        messageModel.setDownVotes(messageEntity.getDownVotes());

        return messageModel;
    }

    public List<MessageModel> entityToModel(List<MessageEntity> messages){
        List<MessageModel> messageModels = new ArrayList<>();
        if(messages != null){
            messages.forEach(m -> messageModels.add(entityToModel(m)));
        }
        return messageModels;

    }
}

package iths.theroom.factory;

import iths.theroom.entity.MessageEntity;
import iths.theroom.model.MessageModel;
import iths.theroom.pojos.MessageForm;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MessageFactory {

    public static MessageModel toModel(MessageEntity messageEntity){
        MessageModel messageModel = new MessageModel();

        messageModel.setUuid(messageEntity.getUuid());
        messageModel.setContent(messageEntity.getContent());
        messageModel.setSender(messageEntity.getSender().getUserName());
        messageModel.setRoom(messageEntity.getRoomEntity().getRoomName());
        messageModel.setTime(messageEntity.getTime().truncatedTo(ChronoUnit.MINUTES).toString());
        if(messageEntity.getType()!=null) {
            messageModel.setType(messageEntity.getType().name());
        }
        messageModel.setRoomBackgroundColor(messageEntity.getRoomEntity().getBackgroundColor());

        Optional.ofNullable(messageEntity.getMessageRatingEntity()).ifPresent(messageRatingEntity -> {
            messageModel.setRating(messageRatingEntity.getRating());
        });

        return messageModel;
    }

    public static List<MessageModel> toModel(List<MessageEntity> messages){
        List<MessageModel> messageModels = new ArrayList<>();
        if(messages != null){
            messages.forEach(m -> messageModels.add(toModel(m)));
        }
        return messageModels;

    }

    public static MessageModel toModel(MessageForm messageForm){
        MessageModel messageModel = new MessageModel();
        messageModel.setContent(messageForm.getContent());
        messageModel.setSender(messageForm.getSender());
        messageModel.setType(messageForm.getType().name());
        messageModel.setTime(Instant.now().toString());
        messageModel.setRating(messageForm.getRating());
        messageModel.setRoomBackgroundColor(messageForm.getRoomBackgroundColor());
        return messageModel;
    }

}

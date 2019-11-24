package iths.theroom.service;

import iths.theroom.entity.MessageEntity;
import iths.theroom.model.MessageModel;

import java.util.List;

public interface MessageService {

    MessageModel getMessageById(Long id);

    MessageModel getMessageByUuid(String uuid);

    List<MessageModel> getAllMessages();

    MessageModel save(MessageEntity messageEntity);

    void remove(MessageEntity messageEntity);

}

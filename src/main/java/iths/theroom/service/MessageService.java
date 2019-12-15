package iths.theroom.service;

import iths.theroom.entity.MessageEntity;
import iths.theroom.model.MessageModel;
import iths.theroom.pojos.MessageForm;

import java.util.List;

public interface MessageService {

    MessageEntity getMessageByUuid(String uuid);
    List<MessageEntity> getAllMessages();
    MessageEntity save(MessageForm form);
    void remove(String uuid);
    List<MessageEntity> getAllMessagesFromUser(String userName, String roomName, String count);
}

package iths.theroom.service;

import iths.theroom.entity.MessageEntity;
import iths.theroom.pojos.MessageForm;

import java.util.List;

public interface MessageService {
    MessageEntity getMessageById(Long id);
    MessageEntity getMessageByUuid(String uuid);
    List<MessageEntity> getAllMessages();
    MessageEntity save(MessageForm form);
    void remove(String uuid);
    List<MessageEntity> getAllMessagesFromUser(String userName);
    List<MessageEntity> getLastMessagesFromUser();
    List<MessageEntity> getAllMessagesFromUserInRoom();
    List<MessageEntity> getLastMessagesFromUserInRoom();
}

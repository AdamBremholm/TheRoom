package iths.theroom.service;

import iths.theroom.entity.MessageEntity;

import java.util.List;

public interface MessageService {

    MessageEntity getMessageById(Long id);

    MessageEntity getMessageByUuid(String uuid);

    List<MessageEntity> getAllMessages();

    MessageEntity save(MessageEntity message);

    void remove(String uuid);

}

package iths.theroom.service;

import iths.theroom.entity.Message;

import java.util.List;

public interface MessageService {

    Message getMessageById(Long id);

    Message getMessageByUuid(String uuid);

    List<Message> getAllMessages();

    Message save(Message message);

    void remove(String uuid);

}

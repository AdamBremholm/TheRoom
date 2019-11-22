package iths.theroom.service;

import iths.theroom.entity.Message;
import iths.theroom.model.MessageModel;

import java.util.List;

public interface MessageService {

    MessageModel getMessageById(Long id);

    MessageModel getMessageByUuid(String uuid);

    List<MessageModel> getAllMessages();

    MessageModel save(Message message);

    void remove(Message message);

}

package iths.theroom.service;

import iths.theroom.dao.MessageRepository;
import iths.theroom.entity.MessageEntity;
import iths.theroom.exception.NoSuchMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public MessageEntity getMessageById(Long id) {
       return messageRepository.findById(id).orElseThrow(NoSuchMessageException::new);
    }

    @Override
    public MessageEntity getMessageByUuid(String uuid) {
        return messageRepository.findByUuid(uuid).orElseThrow(NoSuchMessageException::new);
    }

    @Override
    public List<MessageEntity> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public MessageEntity save(MessageEntity message) {
       return messageRepository.save(message);
    }

    @Override
    public void remove(String uuid) {
        MessageEntity found = getMessageByUuid(uuid);
        messageRepository.delete(found);
    }
}

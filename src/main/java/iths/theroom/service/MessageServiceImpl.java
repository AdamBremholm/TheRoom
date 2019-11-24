package iths.theroom.service;

import iths.theroom.dao.MessageRepository;
import iths.theroom.entity.Message;
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
    public Message getMessageById(Long id) {
       return messageRepository.findById(id).orElseThrow(NoSuchMessageException::new);
    }

    @Override
    public Message getMessageByUuid(String uuid) {
        return messageRepository.findByUuid(uuid).orElseThrow(NoSuchMessageException::new);
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message save(Message message) {
       return messageRepository.save(message);
    }

    @Override
    public void remove(String uuid) {
        Message found = getMessageByUuid(uuid);
        messageRepository.delete(found);
    }
}

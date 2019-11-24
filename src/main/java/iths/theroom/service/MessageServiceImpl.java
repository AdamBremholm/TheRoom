package iths.theroom.service;

import iths.theroom.repository.MessageRepository;
import iths.theroom.entity.Message;
import iths.theroom.model.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public MessageModel getMessageById(Long id) {
       return MessageModel.toModel(messageRepository.findById(id).orElseThrow(NoSuchElementException::new));
    }

    @Override
    public MessageModel getMessageByUuid(String uuid) {
        return MessageModel.toModel(messageRepository.findByUuid(uuid).orElseThrow(NoSuchElementException::new));
    }

    @Override
    public List<MessageModel> getAllMessages() {
        return MessageModel.toModel(messageRepository.findAll());
    }

    @Override
    public MessageModel save(Message message) {
       return MessageModel.toModel(messageRepository.save(message));
    }

    @Override
    public void remove(Message message) {
        messageRepository.delete(message);
    }
}

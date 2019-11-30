package iths.theroom.service;

import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.exception.NoSuchUserException;
import iths.theroom.pojos.MessageForm;
import iths.theroom.repository.MessageRepository;
import iths.theroom.entity.MessageEntity;
import iths.theroom.exception.NoSuchMessageException;
import iths.theroom.repository.RoomRepository;
import iths.theroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;


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
    public MessageEntity save(MessageForm form) {
        UserEntity user = userRepository.findByUserName(form.getUserName()).orElseThrow(NoSuchUserException::new);
        RoomEntity room = roomRepository.getOneByRoomName(form.getRoomName());
        MessageEntity message = new MessageEntity(form.getType(), form.getContent(), user, room);
        messageRepository.save(message);
        room.addMessage(message);
        roomRepository.save(room);
        return message;
    }

    @Override
    public void remove(String uuid) {
        MessageEntity found = getMessageByUuid(uuid);
        messageRepository.delete(found);
    }

    @Override
    public List<MessageEntity> getAllMessagesFromUser(String userName) {
        List<MessageEntity> messagesByUser = new ArrayList<>();
        for(MessageEntity messageEntity: messageRepository.findAll()){
            if(messageEntity.getSender().getUserName().equals(userName)){
                messagesByUser.add(messageEntity);
            }
        }
        return  messagesByUser;
    }

    @Override
    public List<MessageEntity> getLastMessagesFromUser() {
        return null;
    }

    @Override
    public List<MessageEntity> getAllMessagesFromUserInRoom() {
        return null;
    }

    @Override
    public List<MessageEntity> getLastMessagesFromUserInRoom() {
        return null;
    }
}

package iths.theroom.service;

import iths.theroom.entity.MessageRatingEntity;
import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.exception.*;
import iths.theroom.model.MessageModel;
import iths.theroom.pojos.MessageForm;
import iths.theroom.repository.MessageRepository;
import iths.theroom.entity.MessageEntity;
import iths.theroom.repository.RoomRepository;
import iths.theroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static iths.theroom.factory.MessageFactory.toModel;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private RoomRepository roomRepository;

    public MessageServiceImpl(){}

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository, RoomRepository roomRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public MessageModel getMessageByUuid(String uuid) {
        return toModel(messageRepository.findByUuid(uuid).orElseThrow(NoSuchMessageException::new));
    }

    @Override
    public List<MessageModel> getAllMessages() {
        return toModel(messageRepository.findAll());
    }

    @Override
    public MessageModel save(MessageForm form) {
        UserEntity user = userRepository.findByUserName(form.getSender()).orElseThrow(NoSuchUserException::new);
        Optional<RoomEntity> room = roomRepository.getOneByRoomName(form.getRoomName());
        room.orElseThrow(() ->  new BadRequestException("no such room exists"));

        MessageEntity message;
        if (!room.get().getBannedUsers().contains(form.getSender())) {
            message = new MessageEntity(form.getType(), form.getContent(), user, room.get(), new MessageRatingEntity());
            room.get().addMessage(message);
            roomRepository.save(room.get());
            userRepository.save(user);

            return toModel(messageRepository.save(message));
        } else {
            throw new UnauthorizedException("{\"You are banned from chatting in this room!}");
        }
        /*MessageEntity message = new MessageEntity(form.getType(), form.getContent(), user, room.get(), new MessageRatingEntity());
        room.get().addMessage(message);*/

        /*////////remove for production//////////
        //Will be replaced by the rest admin controller
        if(user.getUserName().contains("ban")){
            room.get().banUser(user);
        }
        ///////////////////////////////////////*/

        /*roomRepository.save(room.get());
        userRepository.save(user);
        return toModel(messageRepository.save(message));*/
    }

    @Override
    public void remove(String uuid) {
        Optional<MessageEntity> found = messageRepository.findByUuid(uuid);
        found.ifPresent(messageRepository::delete);
    }

    @Override
    public List<MessageModel> getAllMessagesFromUser(String userName, String roomName, String count) {
        List<MessageEntity> messages = filterByMessagesUsersName(userName);

        if (messages.isEmpty()) {
            throw new NotFoundException("No user found with that username");
        }
        if (roomName != null) {
            try {
                messages = filterByMessagesRoom(messages, roomName);
            } catch (Exception e) {
                throw new NotFoundException("Room not found");
            }
        }
        if (count != null) {
            try {
                int messageCount = Integer.parseInt(count);
                if (messageCount > messages.size()) {
                    throw new NotFoundException("Not enough messages exists for that criteria");
                }
                messages = filterMessagesByCount(messages, messageCount);
            } catch (NumberFormatException e) {
                throw new BadRequestException("Count must be a digit");
            }
        }

        return toModel(messages);
    }

    @Override
    public MessageModel decreaseMessageRating(String uuid, String userName) {

        MessageEntity messageEntity = fetchMessageIfExists(uuid);
        UserEntity userEntity = fetchUserIfExists(userName);

        messageEntity.getMessageRatingEntity().decreaseRating(userEntity);
        messageRepository.save(messageEntity);

        MessageModel messageModel = toModel(messageEntity);
        messageModel.setType("RATING");

        return messageModel;

    }

    @Override
    public MessageModel increaseMessageRating(String uuid, String userName) {

        MessageEntity messageEntity = fetchMessageIfExists(uuid);
        UserEntity userEntity = fetchUserIfExists(userName);

        messageEntity.getMessageRatingEntity().increaseRating(userEntity);
        messageRepository.save(messageEntity);

        MessageModel messageModel = toModel(messageEntity);
        messageModel.setType("RATING");

        return messageModel;

    }

    private List<MessageEntity> filterByMessagesUsersName(String userName) {
        List<MessageEntity> messagesByUser = new ArrayList<>();

        for(MessageEntity messageEntity: messageRepository.findAll()){
            if(messageEntity.getSender().getUserName().equals(userName)){
                messagesByUser.add(messageEntity);
            }
        }
        return messagesByUser;
    }

    private List<MessageEntity> filterByMessagesRoom(List<MessageEntity> messages, String roomName) {
        List<MessageEntity> messagesFilteredByRoomName = new ArrayList<>();

        for (MessageEntity messageEntity : messages) {
            if (messageEntity.getRoomEntity().getRoomName().equals(roomName)) {
                messagesFilteredByRoomName.add(messageEntity);
            }
        }
        return messagesFilteredByRoomName;
    }

    private List<MessageEntity> filterMessagesByCount(List<MessageEntity> messages, int count) {
        List<MessageEntity> messagesByUserLimited = new ArrayList<>();
        int counter = 0;

        for (MessageEntity messageEntity : messages) {
            if(counter  >= count){
                break;
            }
            messagesByUserLimited.add(messageEntity);
            counter++;
        }
        return messagesByUserLimited;
    }

    private MessageEntity fetchMessageIfExists(String messageUuid){
        Optional<MessageEntity> messageFound = messageRepository.findByUuid(messageUuid);
        return messageFound.orElseThrow(NoSuchMessageException::new);
    }

    private UserEntity fetchUserIfExists(String userName){
        Optional<UserEntity> userFound = userRepository.findByUserName(userName);
        return userFound.orElseThrow(NoSuchUserException::new);
    }

    private boolean isBanned(UserEntity user, RoomEntity room){
        return room.getBannedUsers().contains(user);
    }
}

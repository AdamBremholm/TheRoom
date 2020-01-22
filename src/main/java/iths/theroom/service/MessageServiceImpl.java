package iths.theroom.service;

import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.MessageRatingEntity;
import iths.theroom.entity.RoomEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.exception.BadRequestException;
import iths.theroom.exception.NoSuchUserException;
import iths.theroom.exception.NotFoundException;
import iths.theroom.exception.UnauthorizedException;
import iths.theroom.factory.MessageFactory;
import iths.theroom.model.MessageModel;
import iths.theroom.pojos.MessageForm;
import iths.theroom.repository.MessageRepository;
import iths.theroom.repository.RoomRepository;
import iths.theroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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
        return toModel(messageRepository.findByUuid(uuid).orElseThrow(() -> new NotFoundException("Message with uuid " + uuid + " not found")));
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
    }

    @Override
    public void remove(String uuid) {
        Optional<MessageEntity> found = messageRepository.findByUuid(uuid);
        found.ifPresent(messageRepository::delete);
    }

    @Override
    public List<MessageModel> getAllMessagesFromUser(String userName, String roomName, String count) {
        UserEntity user;
        RoomEntity roomEntity;
        List<MessageEntity> messages;
        int messageCount = 0;

        if(count != null) {
            try {
                messageCount = Integer.parseInt(count);

            } catch (NumberFormatException e) {
                throw new BadRequestException("Count must be a digit");
            }
        }

        if(userRepository.findByUserName(userName).isPresent()) {
            user = userRepository.findByUserName(userName).get();
            messages = filterByMessagesUsersName(user);
        }

        else {
            throw new NotFoundException("No user found with that username");
        }

        if(roomName != null) {
            if(roomRepository.getOneByRoomName(roomName).isPresent()) {
                roomEntity = roomRepository.getOneByRoomName(roomName).get();
                messages = filterByMessagesUserAndRoom(user, roomEntity);
            }
            else {
                throw new NotFoundException("Requested user has not posted in that room");
            }
        }
        if(messageCount > messages.size()){
            throw new NotFoundException("Not enough messages exists for that criteria");
        }
        else {
            if( messageCount > 0) {
                messages = filterMessagesByCount(messages, messageCount);
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

    @Override
    public List<MessageModel> findAllByRoomEntityOrderById(String roomName) {
        Optional<RoomEntity> roomFound = roomRepository.getOneByRoomName(roomName);
        if(roomFound.isPresent()){
            return MessageFactory.toModel(messageRepository.findAllByRoomEntityOrderById(roomFound.get()));
        }
        return Collections.emptyList();
    }

    private List<MessageEntity> filterByMessagesUsersName(UserEntity user) {
        return messageRepository.findAllBySenderOrderByTimeDesc(user);
    }

    private List<MessageEntity> filterByMessagesUserAndRoom(UserEntity user, RoomEntity roomEntity) {
        return messageRepository.findAllBySenderAndRoomEntityOrderByTimeDesc(user, roomEntity);
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
        return messageFound.orElseThrow(() -> new NotFoundException("Message with uuid " + messageUuid + " not found"));
    }

    private UserEntity fetchUserIfExists(String userName){
        Optional<UserEntity> userFound = userRepository.findByUserName(userName);
        return userFound.orElseThrow(() -> new NotFoundException("Username " + userName + " not found"));
    }

}

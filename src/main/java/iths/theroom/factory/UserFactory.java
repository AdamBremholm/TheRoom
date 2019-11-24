package iths.theroom.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import iths.theroom.entity.Message;
import iths.theroom.entity.UserEntity;
import iths.theroom.model.MessageModel;
import iths.theroom.model.UserModel;

public class UserFactory {

    public UserEntity jsonToEntity(String userJson){

        ObjectMapper objectMapper = new ObjectMapper();

        try{
           return objectMapper.readValue(userJson, UserEntity.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public UserModel entityToModel(UserEntity userEntity){

        UserModel userModel = new UserModel();
        userModel.setUserName(userEntity.getUserName());
        userModel.setEmail(userEntity.getEmail());
        userModel.setFirstName(userEntity.getFirstName());
        userModel.setLastName(userEntity.getLastName());

        if(userEntity.getMessages() != null){

            for(Message message : userEntity.getMessages()){
                MessageModel messageModel = MessageModel.toModel(message);
                userModel.getMessages().add(messageModel);
            }
        }

        return userModel;
    }
}
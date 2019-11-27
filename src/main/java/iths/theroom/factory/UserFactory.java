package iths.theroom.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.RoleEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.model.MessageModel;
import iths.theroom.model.RoleModel;
import iths.theroom.model.UserModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static iths.theroom.factory.MessageFactory.toModel;

@Component
public class UserFactory {

    public UserEntity jsonToEntity(String userJson){

        ObjectMapper objectMapper = new ObjectMapper();

        try{
           return objectMapper.readValue(userJson, UserEntity.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static UserModel toModel(UserEntity userEntity){

        UserModel userModel = new UserModel();
        userModel.setUserName(userEntity.getUserName());
        userModel.setEmail(userEntity.getEmail());
        userModel.setFirstName(userEntity.getFirstName());
        userModel.setLastName(userEntity.getLastName());

        if(userEntity.getMessages() != null){
            for(MessageEntity message : userEntity.getMessages()){
                MessageModel messageModel = MessageFactory.toModel(message);
                userModel.getMessages().add(messageModel);
            }
        }
        if(userEntity.getRoles()!=null){
           for(RoleEntity role: userEntity.getRoles()){
               RoleModel roleModel = RoleFactory.toModel(role);
               userModel.getRoles().add(roleModel.getRole().name().toLowerCase());
           }
        }

        return userModel;
    }

    public static List<UserModel> toModel(List<UserEntity> users){
        List<UserModel> userModels = new ArrayList<>();
        if(users!=null){
            users.forEach(u -> userModels.add(toModel(u)));
        }
        return userModels;

    }
}

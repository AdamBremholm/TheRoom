package iths.theroom.factory;


import iths.theroom.entity.MessageEntity;
import iths.theroom.entity.UserEntity;
import iths.theroom.model.MessageModel;
import iths.theroom.model.UserModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserFactory {

    public static UserModel toModel(UserEntity userEntity){

        UserModel userModel = new UserModel();
        userModel.setUserName(userEntity.getUserName());
        userModel.setEmail(userEntity.getEmail());
        userModel.setFirstName(userEntity.getFirstName());
        userModel.setLastName(userEntity.getLastName());
        userModel.setRoles(userEntity.getRoles());
        userModel.setPermissions(userEntity.getPermissions());
        userModel.setProfileModel(ProfileFactory.toModel(userEntity.getProfile()));


        if(userEntity.getMessages() != null){
            for(MessageEntity message : userEntity.getMessages()){
                MessageModel messageModel = MessageFactory.toModel(message);
                userModel.getMessages().add(messageModel);
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

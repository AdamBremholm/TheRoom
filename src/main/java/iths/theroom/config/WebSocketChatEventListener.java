package iths.theroom.config;

import iths.theroom.entity.UserEntity;
import iths.theroom.enums.Type;
import iths.theroom.exception.NoSuchUserException;
import iths.theroom.pojos.MessageForm;
import iths.theroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
public class WebSocketChatEventListener {
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private UserService userService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println("Received a new web socket connection");
    }
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");
        if(username != null) {
            UserEntity userEntity;
            try {
                userEntity = userService.getByUserName(username);
            } catch (NoSuchUserException e){
                userEntity = new UserEntity(username);
            }
            MessageForm messageForm = new MessageForm();
            messageForm.setType(Type.LEAVE);
            messageForm.setSender(userEntity.getUserName());
            messagingTemplate.convertAndSend("/topic/public", messageForm);
        }
    }
}
package iths.theroom.security;

import com.sun.security.auth.UserPrincipal;

import iths.theroom.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;

import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

import java.util.UUID;

public class SecDefaultHandshakeHandler extends DefaultHandshakeHandler {

    @Autowired
    UserService userService;


    @Override
    public Principal determineUser(ServerHttpRequest request, WebSocketHandler handler, Map<String, Object> attributes) {
        Object token = attributes.get("token");
        System.out.println("token in determine user: " +token);
        return new UserPrincipal(UUID.randomUUID().toString());
    }
}

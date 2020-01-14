package iths.theroom.security;

import com.auth0.jwt.JWT;
import iths.theroom.entity.UserEntity;
import iths.theroom.exception.UnauthorizedException;
import iths.theroom.service.RoomService;
import iths.theroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;


import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {


    @Autowired
    UserService userService;

    @Autowired
    RoomService roomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null){
            throw new UnauthorizedException("No accessor found");
        }

        if(isUserBanned(message)){
            return null;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String header = null;
            String jwtToken = null;

            List<String> nativeHeaders = accessor.getNativeHeader(JwtProperties.HEADER_STRING);

            if(nativeHeaders!= null && !nativeHeaders.isEmpty()) {
                header = nativeHeaders.get(0);
                if (header != null && header.startsWith(JwtProperties.TOKEN_PREFIX))
                    jwtToken = header.substring(7);
                 else
                    throw new UnauthorizedException("No correctly formatted Authorization header");
            }
            else {
                throw new UnauthorizedException("No correctly formatted Authorization header");
            }

            // If header is present, try grab user principal from database and perform authorization
            try {
                Authentication authentication = getUsernamePasswordAuthentication(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                accessor.setUser(authentication);
            } catch (Exception e){
                throw new UnauthorizedException("Could not validate token");
            }

        }
        return message;
    }

    private Authentication getUsernamePasswordAuthentication(String token) {

        if (token != null) {
            String userName = JWT.require(HMAC512(JwtProperties.SECRET.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();
            if (userName != null) {
                UserEntity user = userService.getByUserName(userName);
                UserPrincipal principal = new UserPrincipal(user);
                return new UsernamePasswordAuthenticationToken(userName, null, principal.getAuthorities());
            }
            return null;
        }
        return null;
    }
    private boolean isUserBanned(Message<?> message){

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");
        String roomUrl = headerAccessor.getDestination();
        String roomUrlSegments[];
        String roomName;

        if(username == null){
            return false;
        }

        if(roomUrl != null && roomUrl.contains(".")) {
            roomUrlSegments = roomUrl.split("\\.");
            int segments = roomUrlSegments.length;
            System.out.println(segments);

            if(segments > 0) {
                roomName = roomUrlSegments[segments - 1];
                return roomService.isUserBannedHere(username, roomName);
            }
        }
        return false;
    }
}
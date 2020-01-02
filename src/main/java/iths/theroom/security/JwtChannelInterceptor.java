package iths.theroom.security;

import io.jsonwebtoken.ExpiredJwtException;
import iths.theroom.exception.UnauthorizedException;
import iths.theroom.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import java.util.Objects;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {


    @Autowired
    UserService userService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        String username = null;
        String jwtToken = null;
        final Log logger = LogFactory.getLog(this.getClass());

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String requestTokenHeader = Objects.requireNonNull(accessor.getNativeHeader("Authorization")).get(0);

            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {

                jwtToken = requestTokenHeader.substring(7);
                try {
                    username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                } catch (IllegalArgumentException e) {
                    throw new UnauthorizedException("");
                } catch (ExpiredJwtException e) {
                    throw new UnauthorizedException("Token has expired");
                } catch (Exception e) {
                    throw new UnauthorizedException("Token not valid");
                }
            } else {
                logger.warn("JWT Token does not begin with Bearer String");
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                try {
                    UserDetails userDetails = userService.loadUserByUsername(username);
                    final boolean valid = jwtTokenUtil.validateToken(jwtToken, userDetails);

                    if (valid) {

                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        Authentication user = SecurityContextHolder.getContext().getAuthentication();
                        accessor.setUser(user);
                    }
                    else {
                        throw new UnauthorizedException("Token not valid");
                    }
                } catch (Exception e){
                    throw new UnauthorizedException("Unable to validate token");
                }

            }

            else {
                throw new UnauthorizedException("No User found");
            }

        }
        return message;
    }



}

package com.example.diplomayinjava.config;

import com.example.diplomayinjava.security.auth.service.CustomUserDetailsService;
import com.example.diplomayinjava.security.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> authHeaders = accessor.getNativeHeader("Authorization");
            
            if (authHeaders != null && !authHeaders.isEmpty()) {
                String authHeader = authHeaders.get(0);
                log.info("🔐 WebSocket CONNECT with Authorization header");
                
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    try {
                        String jwt = authHeader.substring(7);
                        String userEmail = jwtService.extractUsername(jwt);
                        log.info("🔐 Extracted user email from JWT: {}", userEmail);
                        
                        if (userEmail != null) {
                            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                            
                            if (jwtService.isTokenValid(jwt, userDetails)) {
                                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                                );
                                
                                accessor.setUser(authToken);
                                log.info("✅ WebSocket authenticated for user: {}", userEmail);
                            } else {
                                log.warn("⚠️ Invalid JWT token for WebSocket connection");
                            }
                        }
                    } catch (Exception e) {
                        log.error("❌ Error authenticating WebSocket connection: {}", e.getMessage());
                    }
                }
            } else {
                log.warn("⚠️ WebSocket CONNECT without Authorization header");
            }
        }
        
        return message;
    }
}


package com.example.diplomayinjava.controller;

import com.example.diplomayinjava.dto.ChatMessageDto;
import com.example.diplomayinjava.dto.ChatNotification;
import com.example.diplomayinjava.security.auth.CurrentUser;
import com.example.diplomayinjava.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    
    /**
     * Обработка входящих сообщений (точно как в bank)
     */
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDto chatMessageDto, java.security.Principal principal) {
        log.info("🏠 Processing chat message: {}", chatMessageDto);
        log.info("🔐 Principal from WebSocket: {}", principal != null ? principal.getName() : "null");
        
        try {
            // Проверка авторизации - сначала пробуем из Principal, потом из SecurityContext
            CurrentUser currentUser = null;
            
            if (principal != null && principal instanceof UsernamePasswordAuthenticationToken) {
                UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) principal;
                if (authToken.getPrincipal() instanceof CurrentUser) {
                    currentUser = (CurrentUser) authToken.getPrincipal();
                    // Устанавливаем в SecurityContext для совместимости
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("✅ Using Principal from WebSocket session");
                }
            }
            
            // Если не получили из Principal, пробуем из SecurityContext
            if (currentUser == null) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CurrentUser) {
                    currentUser = (CurrentUser) authentication.getPrincipal();
                    log.info("✅ Using Principal from SecurityContext");
                }
            }
            
            if (currentUser == null) {
                log.warn("⚠️ Unauthorized WebSocket message attempt - no authentication found");
                return;
            }
            log.info("👤 Authenticated user: {} (ID: {})", currentUser.getEmail(), currentUser.getId());
            
            // Валидация сообщения
            if (chatMessageDto.getContent() == null || chatMessageDto.getContent().trim().isEmpty()) {
                log.warn("⚠️ Empty message content, ignoring");
                return;
            }
            
            if (chatMessageDto.getSenderId() == null) {
                log.warn("⚠️ No sender ID, ignoring");
                return;
            }
            
            if (chatMessageDto.getReceiverId() == null) {
                log.warn("⚠️ No receiver ID, ignoring");
                return;
            }
            
            // Проверка: пользователь не может писать сам себе
            if (chatMessageDto.getSenderId().equals(chatMessageDto.getReceiverId())) {
                log.warn("⚠️ User {} attempted to send message to themselves", chatMessageDto.getSenderId());
                return;
            }
            
            // Проверка: отправитель должен быть текущим авторизованным пользователем
            if (!chatMessageDto.getSenderId().equals(currentUser.getId())) {
                log.warn("⚠️ User {} attempted to send message as user {}", currentUser.getId(), chatMessageDto.getSenderId());
                return;
            }
            
            if (chatMessageDto.getMessageType() == null) {
                chatMessageDto.setMessageType("TEXT");
            }
            
        // Сохраняем сообщение в базе данных
        ChatMessageDto savedMessage = chatService.sendMessage(chatMessageDto);
            log.info("✅ Message saved successfully: {}", savedMessage);
        
            // Отправляем уведомление получателю (точно как в bank)
        if (savedMessage.getReceiverId() != null) {
                ChatNotification notification = ChatNotification.builder()
                        .id(savedMessage.getId())
                        .senderId(savedMessage.getSenderId().toString())
                        .receiverId(savedMessage.getReceiverId().toString())
                        .content(savedMessage.getContent())
                        .build();
                
                log.info("📤 Sending notification to user {}: {}", savedMessage.getReceiverId(), notification);
                
                // Отправляем получателю через общий топик (проще и надежнее)
                String destination = "/topic/user/" + savedMessage.getReceiverId();
                messagingTemplate.convertAndSend(destination, notification);
                
                log.info("✅ Message notification sent to topic: {}", destination);
            } else {
                log.warn("⚠️ No receiver ID, cannot send notification");
            }
        } catch (Exception e) {
            log.error("❌ Error processing message: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Получить сообщения между двумя пользователями с пагинацией
     */
    @GetMapping("/messages/{senderId}/{recipientId}/paginated")
    public ResponseEntity<Page<ChatMessageDto>> findChatMessagesWithPagination(
            @PathVariable("senderId") String senderId, 
            @PathVariable("recipientId") String recipientId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "15") int size) {
        try {
            // Проверка авторизации
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof CurrentUser)) {
                log.warn("⚠️ Unauthorized request to get messages");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
            log.info("👤 Authenticated user: {} (ID: {})", currentUser.getEmail(), currentUser.getId());
            
            log.info("📋 Getting paginated chat messages between users: {} and {} (page={}, size={})", senderId, recipientId, page, size);
            Long senderIdLong = Long.parseLong(senderId);
            Long recipientIdLong = Long.parseLong(recipientId);
            log.info("📋 Parsed IDs: sender={}, recipient={}", senderIdLong, recipientIdLong);
            
            // Проверка: текущий пользователь должен быть одним из участников чата
            if (!currentUser.getId().equals(senderIdLong) && !currentUser.getId().equals(recipientIdLong)) {
                log.warn("⚠️ User {} attempted to access messages between users {} and {}", currentUser.getId(), senderIdLong, recipientIdLong);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            Page<ChatMessageDto> messages = chatService.getChatMessagesBetweenUsersWithPagination(senderIdLong, recipientIdLong, page, size);
            log.info("📋 Found {} messages on page {} of {}", messages.getContent().size(), page, messages.getTotalPages());
            
            return ResponseEntity.ok(messages);
        } catch (NumberFormatException e) {
            log.error("❌ Invalid user ID format: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("❌ Error getting paginated chat messages: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Получить сообщения между двумя пользователями (старый метод для обратной совместимости)
     */
    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessageDto>> findChatMessages(@PathVariable("senderId") String senderId, @PathVariable("recipientId") String recipientId) {
        try {
            // Проверка авторизации
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof CurrentUser)) {
                log.warn("⚠️ Unauthorized request to get messages");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
            log.info("👤 Authenticated user: {} (ID: {})", currentUser.getEmail(), currentUser.getId());
            
            log.info("📋 Getting chat messages between users: {} and {}", senderId, recipientId);
            Long senderIdLong = Long.parseLong(senderId);
            Long recipientIdLong = Long.parseLong(recipientId);
            log.info("📋 Parsed IDs: sender={}, recipient={}", senderIdLong, recipientIdLong);
            
            // Проверка: текущий пользователь должен быть одним из участников чата
            if (!currentUser.getId().equals(senderIdLong) && !currentUser.getId().equals(recipientIdLong)) {
                log.warn("⚠️ User {} attempted to access messages between users {} and {}", currentUser.getId(), senderIdLong, recipientIdLong);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            List<ChatMessageDto> messages = chatService.getChatMessagesBetweenUsers(senderIdLong, recipientIdLong);
            log.info("📋 Found {} messages", messages.size());
            
            return ResponseEntity.ok(messages);
        } catch (NumberFormatException e) {
            log.error("❌ Invalid user ID format: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("❌ Error getting chat messages: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
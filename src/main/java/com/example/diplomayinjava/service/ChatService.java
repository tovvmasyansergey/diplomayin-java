package com.example.diplomayinjava.service;

import com.example.diplomayinjava.dto.ChatMessageDto;
import com.example.diplomayinjava.entity.AppUser;
import com.example.diplomayinjava.entity.Message;
import com.example.diplomayinjava.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    /**
     * Сохранить сообщение (точно как в bank)
     */
    public ChatMessageDto sendMessage(ChatMessageDto messageDto) {
        log.info("💬 Sending message: {}", messageDto);
        log.info("SenderId: {}, ReceiverId: {}", messageDto.getSenderId(), messageDto.getReceiverId());
        
        // Получаем отправителя и получателя
        AppUser sender = userService.findById(messageDto.getSenderId());
        AppUser receiver = userService.findById(messageDto.getReceiverId());
        
        log.info("👤 Sender: {}, Receiver: {}", sender.getEmail(), receiver.getEmail());
        
        // Создаем сообщение
        Message message = Message.builder()
                .content(messageDto.getContent())
                .sender(sender)
                .receiver(receiver)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .messageType(messageDto.getMessageType() != null ? 
                    Message.MessageType.valueOf(messageDto.getMessageType()) : 
                    Message.MessageType.TEXT)
                .build();

        // Сохраняем в базе данных
        Message savedMessage = messageRepository.save(message);
        log.info("💾 Message saved with ID: {}", savedMessage.getId());
        
        // Конвертируем в DTO
        ChatMessageDto result = convertMessageToDto(savedMessage);
        log.info("📤 Returning message DTO: {}", result);
        
        return result;
    }

    /**
     * Найти сообщения между двумя пользователями (точно как в bank)
     */
    @Transactional(readOnly = true)
    public List<ChatMessageDto> getChatMessagesBetweenUsers(Long userId1, Long userId2) {
        log.info("📋 Finding messages between users: {} and {}", userId1, userId2);
        List<Message> messages = messageRepository.findMessagesBetweenUsers(userId1, userId2);
        log.info("📋 Found {} messages", messages.size());
        return messages.stream()
                .map(this::convertMessageToDto)
                .collect(Collectors.toList());
    }

    /**
     * Конвертировать Message в ChatMessageDto
     */
    private ChatMessageDto convertMessageToDto(Message message) {
        return ChatMessageDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getFirstname() + " " + message.getSender().getLastname())
                .senderEmail(message.getSender().getEmail())
                .receiverId(message.getReceiver().getId())
                .receiverName(message.getReceiver().getFirstname() + " " + message.getReceiver().getLastname())
                .receiverEmail(message.getReceiver().getEmail())
                .createdAt(message.getCreatedAt())
                .isRead(message.getIsRead())
                .messageType(message.getMessageType() != null ? message.getMessageType().name() : "TEXT")
                .build();
    }
}
package com.example.diplomayinjava.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {
    
    private Long id;
    private String roomName;
    private Long user1Id;
    private String user1Name;
    private String user1Email;
    private Long user2Id;
    private String user2Name;
    private String user2Email;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private List<ChatMessageDto> messages;
    private Long unreadCount;
    private ChatMessageDto lastMessage;
}

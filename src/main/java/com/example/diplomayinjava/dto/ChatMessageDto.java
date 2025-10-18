package com.example.diplomayinjava.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {
    private Long id;
    private String content;
    private Long senderId;
    private String senderName;
    private String senderEmail;
    @JsonProperty("recipientId")
    private Long receiverId;
    private String receiverName;
    private String receiverEmail;
    private LocalDateTime createdAt;
    private Boolean isRead;
    private String messageType;
}
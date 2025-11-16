package com.example.diplomayinjava.service;

import com.example.diplomayinjava.dto.ChatMessageDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface ChatService {

    /**
     * Сохранить сообщение (точно как в bank)
     */
    public ChatMessageDto sendMessage(ChatMessageDto messageDto);

    /**
     * Найти сообщения между двумя пользователями с пагинацией
     */
    @Transactional(readOnly = true)
    public Page<ChatMessageDto> getChatMessagesBetweenUsersWithPagination(Long userId1, Long userId2, int page, int size);

    /**
     * Найти сообщения между двумя пользователями (старый метод для обратной совместимости)
     */
    @Transactional(readOnly = true)
    public List<ChatMessageDto> getChatMessagesBetweenUsers(Long userId1, Long userId2);

}
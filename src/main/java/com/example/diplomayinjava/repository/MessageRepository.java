package com.example.diplomayinjava.repository;

import com.example.diplomayinjava.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    // Найти все сообщения в чат-комнате
    @Query("SELECT m FROM Message m WHERE m.chatRoom.id = :chatRoomId ORDER BY m.createdAt ASC")
    List<Message> findByChatRoomIdOrderByCreatedAtAsc(@Param("chatRoomId") Long chatRoomId);
    
    // Найти сообщения между двумя пользователями с пагинацией
    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
           "(m.sender.id = :userId2 AND m.receiver.id = :userId1) " +
           "ORDER BY m.createdAt DESC")
    Page<Message> findMessagesBetweenUsersWithPagination(@Param("userId1") Long userId1, @Param("userId2") Long userId2, Pageable pageable);
    
    // Найти сообщения между двумя пользователями (старый метод для обратной совместимости)
    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
           "(m.sender.id = :userId2 AND m.receiver.id = :userId1) " +
           "ORDER BY m.createdAt ASC")
    List<Message> findMessagesBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
    
    // Найти непрочитанные сообщения для пользователя
    @Query("SELECT m FROM Message m WHERE m.receiver.id = :userId AND m.isRead = false")
    List<Message> findUnreadMessagesByUserId(@Param("userId") Long userId);
    
    // Подсчитать непрочитанные сообщения
    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiver.id = :userId AND m.isRead = false")
    Long countUnreadMessagesByUserId(@Param("userId") Long userId);
    
    // Найти последнее сообщение в чат-комнате
    @Query("SELECT m FROM Message m WHERE m.chatRoom.id = :chatRoomId ORDER BY m.createdAt DESC")
    Page<Message> findLastMessageByChatRoomId(@Param("chatRoomId") Long chatRoomId, Pageable pageable);
    
}

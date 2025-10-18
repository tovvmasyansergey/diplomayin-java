package com.example.diplomayinjava.repository;

import com.example.diplomayinjava.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    
    // Найти чат-комнату между двумя пользователями
    @Query("SELECT cr FROM ChatRoom cr WHERE " +
           "(cr.user1.id = :userId1 AND cr.user2.id = :userId2) OR " +
           "(cr.user1.id = :userId2 AND cr.user2.id = :userId1)")
    Optional<ChatRoom> findChatRoomBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
    
    // Найти все чат-комнаты пользователя
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.user1.id = :userId OR cr.user2.id = :userId ORDER BY cr.createdAt DESC")
    List<ChatRoom> findChatRoomsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT cr FROM ChatRoom cr WHERE (cr.user1.id = :userId OR cr.user2.id = :userId) AND cr.isActive = true ORDER BY cr.createdAt DESC")
    List<ChatRoom> findActiveChatRoomsByUserId(@Param("userId") Long userId);
    
    // Проверить существование чат-комнаты между пользователями
    @Query("SELECT COUNT(cr) > 0 FROM ChatRoom cr WHERE " +
           "(cr.user1.id = :userId1 AND cr.user2.id = :userId2) OR " +
           "(cr.user1.id = :userId2 AND cr.user2.id = :userId1)")
    boolean existsChatRoomBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}

package ru.kpfu.itis.kononenko.gtree2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.kononenko.gtree2.entity.Conversation;
import ru.kpfu.itis.kononenko.gtree2.entity.PrivateMessage;

import java.util.List;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {
    List<PrivateMessage> findByConversationOrderByCreatedAt(Conversation conversation);
    void deleteAllByConversation(Conversation conversation);
}
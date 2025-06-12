package ru.kpfu.itis.kononenko.gtree2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.kononenko.gtree2.entity.Conversation;
import ru.kpfu.itis.kononenko.gtree2.entity.User;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByUser1AndUser2(User user1, User user2);
    Optional<Conversation> findByUser1AndUser2OrUser1AndUser2(User u1, User u2, User u3, User u4);
}
package ru.kpfu.itis.kononenko.gtree2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.kononenko.gtree2.dto.response.MessageResponse;
import ru.kpfu.itis.kononenko.gtree2.entity.Conversation;
import ru.kpfu.itis.kononenko.gtree2.entity.PrivateMessage;
import ru.kpfu.itis.kononenko.gtree2.entity.User;
import ru.kpfu.itis.kononenko.gtree2.exception.NotFoundException;
import ru.kpfu.itis.kononenko.gtree2.repository.ConversationRepository;
import ru.kpfu.itis.kononenko.gtree2.repository.PrivateMessageRepository;
import ru.kpfu.itis.kononenko.gtree2.repository.UserRepository;
import ru.kpfu.itis.kononenko.gtree2.service.DialogService;
import ru.kpfu.itis.kononenko.gtree2.service.security.CustomUserDetails;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DialogServiceImpl implements DialogService {

    private final ConversationRepository conversationRepository;
    private final PrivateMessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public List<MessageResponse> getMessages(String username) {
        User current = getCurrentUser();
        User other = getUser(username);
        Conversation conv = getOrCreate(current, other);
        return messageRepository.findByConversationOrderByCreatedAt(conv)
                .stream()
                .map(m -> MessageResponse.builder()
                        .id(m.getId())
                        .sender(m.getSender().getUsername())
                        .content(m.getContent())
                        .createdAt(m.getCreatedAt().toString())
                        .build())
                .toList();
    }

    @Override
    public MessageResponse sendMessage(String username, String content) {
        User current = getCurrentUser();
        User other = getUser(username);
        Conversation conv = getOrCreate(current, other);
        PrivateMessage msg = new PrivateMessage();
        msg.setConversation(conv);
        msg.setSender(current);
        msg.setContent(content);
        msg = messageRepository.save(msg);
        return MessageResponse.builder()
                .id(msg.getId())
                .sender(current.getUsername())
                .content(msg.getContent())
                .createdAt(msg.getCreatedAt().toString())
                .build();
    }

    @Override
    public Long ensureConversation(String username) {
        User current = getCurrentUser();
        User other = getUser(username);
        return getOrCreate(current, other).getId();
    }

    private Conversation getOrCreate(User a, User b) {
        return conversationRepository
                .findByUser1AndUser2OrUser1AndUser2(a, b, b, a)
                .orElseGet(() -> {
                    Conversation c = new Conversation();
                    c.setUser1(a);
                    c.setUser2(b);
                    return conversationRepository.save(c);
                });
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails det)) {
            throw new RuntimeException("User not authenticated");
        }
        return det.getUser();
    }
}
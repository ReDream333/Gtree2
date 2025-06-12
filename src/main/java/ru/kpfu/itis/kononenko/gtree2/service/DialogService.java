package ru.kpfu.itis.kononenko.gtree2.service;

import ru.kpfu.itis.kononenko.gtree2.dto.response.MessageResponse;

import java.util.List;

public interface DialogService {
    List<MessageResponse> getMessages(String username);
    MessageResponse sendMessage(String username, String content);
    Long ensureConversation(String username);
}
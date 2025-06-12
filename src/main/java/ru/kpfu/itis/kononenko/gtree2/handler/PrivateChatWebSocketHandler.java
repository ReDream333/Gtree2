package ru.kpfu.itis.kononenko.gtree2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.kpfu.itis.kononenko.gtree2.dto.response.MessageResponse;
import ru.kpfu.itis.kononenko.gtree2.service.DialogService;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class PrivateChatWebSocketHandler extends TextWebSocketHandler {

    private final DialogService dialogService;
    private final ObjectMapper mapper = new ObjectMapper();

    private final Map<Long, Set<WebSocketSession>> conversations = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, Long> sessionConv = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, String> sessionTarget = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String path = Optional.ofNullable(session.getUri()).map(URI::getPath).orElse("");
        String username = path.substring(path.lastIndexOf('/') + 1);
        if (session.getPrincipal() instanceof Authentication auth) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            try {
                Long convId = dialogService.ensureConversation(username);
                conversations.computeIfAbsent(convId, k -> ConcurrentHashMap.newKeySet()).add(session);
                sessionConv.put(session, convId);
                sessionTarget.put(session, username);
            } finally {
                SecurityContextHolder.clearContext();
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String target = sessionTarget.get(session);
        Long convId = sessionConv.get(session);
        if (target == null || convId == null) {
            return;
        }
        if (session.getPrincipal() instanceof Authentication auth) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            try {
                MessageResponse resp = dialogService.sendMessage(target, message.getPayload());
                String json = mapper.writeValueAsString(resp);
                for (WebSocketSession s : conversations.getOrDefault(convId, Set.of())) {
                    if (s.isOpen()) {
                        s.sendMessage(new TextMessage(json));
                    }
                }
            } finally {
                SecurityContextHolder.clearContext();
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long convId = sessionConv.remove(session);
        sessionTarget.remove(session);
        if (convId != null) {
            Set<WebSocketSession> set = conversations.get(convId);
            if (set != null) {
                set.remove(session);
                if (set.isEmpty()) {
                    conversations.remove(convId);
                }
            }
        }
    }
}
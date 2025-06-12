package ru.kpfu.itis.kononenko.gtree2.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.security.core.Authentication;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        if (session.getPrincipal() instanceof Authentication) {
            sessions.add(session);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String username = "anonymous";
        if (session.getPrincipal() instanceof Authentication auth) {
            username = auth.getName();
        }
        TextMessage outgoing = new TextMessage(username + ": " + payload);
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(outgoing);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }
}
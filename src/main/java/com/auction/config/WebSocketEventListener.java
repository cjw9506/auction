package com.auction.config;

import com.auction.chat.domain.ChatMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        Long roomId = (Long) headerAccessor.getSessionAttributes().get("roomId");

        if (username != null && roomId != null) {

            //TODO 메시지 생성 및 저장 로직
//            ChatMessage message = ChatMessage.builder()
//                    .sender(username)
//                    .message("방을 나갔습니다.")
//                    .build();
//
//            messageTemplate.convertAndSend("/sub/" + roomId, message);

            //TODO 커넥트 끊을 때 날라가는 메시지에 chatMessage id -> null 나중에 개선

        }
    }
}

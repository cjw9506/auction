package com.auction.chat.controller;

import com.auction.chat.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template;

    //처음 방에 입장했을 때의 로직
    @MessageMapping("/enter/{roomId}")
    public void enter(@DestinationVariable Long roomId,
                      MessageDTO message,
                      SimpMessageHeaderAccessor headerAccessor) {

        template.convertAndSend("/sub/" + message.getRoomId(), message);

        headerAccessor.getSessionAttributes().put("username", message.getSender());
        headerAccessor.getSessionAttributes().put("roomId", message.getRoomId());
        //TODO 방 제한인원 감소 로직 작성

    }

    //방 입장 후 채팅할 때의 로직
    @MessageMapping("/chat/{roomId}")
    public void chat(@DestinationVariable Long roomId,
                     MessageDTO message) {

        template.convertAndSend("/sub/" + message.getRoomId(), message);


        //TODO 채팅 이후 최고가 갱신 로직 작성

    }


}

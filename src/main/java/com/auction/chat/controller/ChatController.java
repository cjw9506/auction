package com.auction.chat.controller;

import com.auction.chat.dto.MessageDTO;
import com.auction.chat.service.ChatService;
import com.auction.room.service.AuctionRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template;
    private final ChatService chatService;
    private final AuctionRoomService auctionRoomService;




    @MessageMapping("/chat.addUser/{roomId}")
    public void addUser(@Payload MessageDTO message,
                        @DestinationVariable String roomId,
                        SimpMessageHeaderAccessor headerAccessor) {

        if (chatService.findUserAndRoom(message)) {
            headerAccessor.getSessionAttributes().put("username", message.getSender());
            headerAccessor.getSessionAttributes().put("roomId", message.getRoomId());
            template.convertAndSend("/sub/public/" + roomId, message);
        }

        //TODO 진입 시 현재가 확인
        //TODO 방 제한인원 감소 로직 작성

    }


    @MessageMapping("/chat.sendMessage/{roomId}")
    public void sendMessage(
            @Payload MessageDTO message,
            @DestinationVariable String roomId) {

        boolean isHighest = chatService.isHighestPrice(message);
        message.updateStatus(isHighest);

        if (isHighest) {
            template.convertAndSend("/sub/public/" + message.getRoomId(), message);
            chatService.save(message);
            auctionRoomService.changeHighestUser(roomId, message.getSender());
        }
    }


}

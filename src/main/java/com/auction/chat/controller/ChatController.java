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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template;
    private final ChatService chatService;
    private final AuctionRoomService auctionRoomService;
    static ExecutorService executor = Executors.newFixedThreadPool(10); // 작업을 처리할 스레드 풀 생성



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

        CompletableFuture<Boolean> isHighestFuture = CompletableFuture.supplyAsync(() -> chatService.isHighestPrice(message), executor);
        isHighestFuture.thenAccept(isHighest -> {
            message.updateStatus(isHighest);

            if (isHighest) {
                executor.submit(() -> {
                    template.convertAndSend("/sub/public/" + message.getRoomId(), message);
                });
                executor.submit(() -> {
                    chatService.save(message);
                });
                executor.submit(() -> {
                    auctionRoomService.changeHighestUser(roomId, message.getSender());
                });
            }
        });
    }
}

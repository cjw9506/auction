package com.auction.chat.service;

import com.auction.auth.domain.User;
import com.auction.auth.repository.UserRepository;
import com.auction.chat.domain.ChatMessage;
import com.auction.chat.dto.MessageDTO;
import com.auction.chat.repository.ChatRepository;
import com.auction.room.domain.AuctionRoom;
import com.auction.room.dto.EnterAuctionRoomRequest;
import com.auction.room.service.AuctionRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatService {

    private final StringRedisTemplate redisTemplate;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    private final AuctionRoomService auctionRoomService;

    // 유저 및 경매방 찾고 Redis에 추가
    public boolean findUserAndRoom(MessageDTO message) {
        User user = userRepository.findByAccount(message.getSender()).
                orElseThrow(() -> new IllegalArgumentException("계정 없음"));
//        AuctionRoom auctionRoom = auctionRoomRepository.findById(message.getRoomId()).
//                orElseThrow(() -> new IllegalArgumentException(("경매방 없음")));
        //TODO auctionroom -> redis

        //TODO 진입 시 -> 최고가 불러와서 클라이언트에 쏴줌 -> 클라이언트는 동적으로 방 제목 옆에 띄우던가

        EnterAuctionRoomRequest request = new EnterAuctionRoomRequest(user.getId());
        auctionRoomService.enterAuctionRoom(request, message.getRoomId());

        return true;
    }

    @Transactional
    public boolean isHighestPrice(MessageDTO message) {
        String key = "auction:room:" + message.getRoomId() + ":info";
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

        Double currentEndPrice = getDouble(entries, "endPrice");
        Double startPrice = getDouble(entries, "startPrice");

        Optional<User> userOpt = userRepository.findByAccount(message.getSender());
        if (userOpt.isEmpty()) {
            return false;
        }

        if (currentEndPrice < message.getContent()) {
            AuctionRoom auctionRoom = buildAuctionRoom(message, entries, startPrice, userOpt.get());
            redisTemplate.opsForHash().putAll(auctionRoom.toInfoKeyString(message.getRoomId()), auctionRoom.toValueMap());
            Map<Object, Object> changeEntries = redisTemplate.opsForHash().entries(key);

            log.info("현재 최고가 {}" , getDouble(changeEntries, "endPrice"));

            return true;
        } else {
            log.info("최고가 변동 없음");
            return false;
        }
    }

    @Transactional
    public void save(MessageDTO message) {
        //TODO AuctionRoom entity 추가
        ChatMessage chatMessage = ChatMessage.builder()
                .content(message.getContent())
                .sender(message.getSender())
                .build();

        chatRepository.save(chatMessage);
    }

    private AuctionRoom buildAuctionRoom(MessageDTO message, Map<Object, Object> entries, Double startPrice, User user) {
        return AuctionRoom.builder()
                .uuid(message.getRoomId())
                .itemName((String) entries.get("itemName"))
                .highestBidUser(user)
                .startPrice(startPrice)
                .endPrice(message.getContent())
                .startTimestamp(getLong(entries, "startTimestamp"))
                .endTimestamp(getLong(entries, "endTimestamp"))
                .build();
    }
    private Double getDouble(Map<Object, Object> map, String key) {
        return Optional.ofNullable((String) map.get(key))
                .map(Double::valueOf)
                .orElse(null);
    }

    private Long getLong(Map<Object, Object> map, String key) {
        return Optional.ofNullable((String) map.get(key))
                .map(Long::valueOf)
                .orElse(null);
    }

}

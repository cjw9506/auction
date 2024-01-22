package com.auction.chat.service;

import com.auction.chat.dto.MessageDTO;
import com.auction.room.domain.AuctionRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final StringRedisTemplate redisTemplate;

    @Transactional
    public boolean isHighestPrice(MessageDTO message) {
        String key = "auction:room:" + message.getRoomId() + ":info";
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

        Double currentEndPrice = getDouble(entries, "endPrice");
        Double startPrice = getDouble(entries, "startPrice");
        Long startTimestamp = getLong(entries, "startTimestamp");
        Long endTimestamp = getLong(entries, "endTimestamp");

        if (currentEndPrice < message.getContent()) {
            AuctionRoom auctionRoom = AuctionRoom.builder()
                    .uuid(message.getRoomId())
                    .itemName((String) entries.get("itemName"))
                    .highestBidUserId(message.getSender())
                    .startPrice(startPrice)
                    .endPrice(message.getContent())
                    .startTimestamp(startTimestamp)
                    .endTimestamp(endTimestamp)
                    .build();

            redisTemplate.opsForHash().putAll(auctionRoom.toInfoKeyString(message.getRoomId()), auctionRoom.toValueMap());

            return true;
        } else {
            return false;
        }

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

package com.auction.room.service;

import com.auction.room.domain.AuctionRoom;
import com.auction.room.dto.AddAuctionRoomRequest;
import com.auction.room.dto.EnterAuctionRoomRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuctionRoomService {
    private final StringRedisTemplate redisTemplate;

    // TODO : MySQL 추가?
    public ResponseEntity<AuctionRoom> addAuctionRoom(AddAuctionRoomRequest request) {
        AuctionRoom auctionRoom = AuctionRoom.builder()
                .startPrice(request.startPrice())
                .endTimestamp(request.endTimestamp())
                .itemName(request.itemName())
                .build();

        redisTemplate.opsForHash().putAll(auctionRoom.toKeyString(), auctionRoom.toValueMap());
        return ResponseEntity.ok().body(auctionRoom);
    }


    // TODO : 지금은 유저 id 리스트로 반환, 후에 유저엔티티 만들어 진다면 수정
    public ResponseEntity<Set<String>> enterAuctionRoom(EnterAuctionRoomRequest request, String roomId) {
        redisTemplate.opsForSet()
                .add("auction:room:" + roomId, request.userId().toString());

        return ResponseEntity.ok().body(redisTemplate.opsForSet()
                .members("auction:room:" + roomId));
    }
}

package com.auction.room.service;

import com.auction.room.domain.AuctionRoom;
import com.auction.room.dto.AddAuctionRoomRequest;
import com.auction.room.dto.AuctionRoomResponse;
import com.auction.room.dto.EnterAuctionRoomRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuctionRoomService {
    private final StringRedisTemplate redisTemplate;
    private List<Object> multiGet = List.of("itemName", "startPrice", "endPrice", "startTimestamp", "endTimestamp");


    // TODO : MySQL 추가?
    public ResponseEntity<AuctionRoom> addAuctionRoom(AddAuctionRoomRequest request) {
        AuctionRoom auctionRoom = AuctionRoom.builder()
                .startPrice(request.startPrice())
                .endTimestamp(request.endTimestamp())
                .itemName(request.itemName())
                .build();

        redisTemplate.opsForHash().putAll(auctionRoom.toInfoKeyString(), auctionRoom.toValueMap());
        return ResponseEntity.ok().body(auctionRoom);
    }


    // TODO : 지금은 유저 id 리스트로 반환, 후에 유저엔티티 만들어 진다면 수정
    public Set<String> enterAuctionRoom(EnterAuctionRoomRequest request, String roomId) {
        redisTemplate.opsForSet()
                .add("auction:room:" + roomId, request.userId().toString());

        return redisTemplate.opsForSet()
                .members("auction:room:" + roomId);
    }

    /**
     * 방 목록 반환
     * 방 id, 아이템 이름, 시작가, 시작/ 마감 시간, 인원
     */
    public List<AuctionRoomResponse> findAuctionRoom() {
        List<AuctionRoomResponse> result = new ArrayList<>();

        try (
                Cursor<String> scan = redisTemplate.scan(ScanOptions.scanOptions().match(
                        "auction:room:*:info").build());
        ) {
            while (scan.hasNext()) {
                String next = scan.next();
                List<Object> hashVal = redisTemplate.opsForHash().multiGet(next, multiGet);
                Long numberOfPeople = redisTemplate.opsForSet().size(next.substring(0, 49));
                String uuid = next.substring(13, 49);

                result.add(AuctionRoomResponse.of(uuid,
                        (String) hashVal.get(0),
                        (String) hashVal.get(1),
                        (String) hashVal.get(2),
                        (String) hashVal.get(3),
                        (String) hashVal.get(4),
                        String.valueOf(numberOfPeople)
                ));
            }
        }

        return result;
    }
}

package com.auction.room.service;

import com.auction.auth.domain.User;
import com.auction.auth.repository.UserRepository;
import com.auction.room.domain.AuctionRoom;
import com.auction.room.dto.AddAuctionRoomRequest;
import com.auction.room.dto.AuctionRoomResponse;
import com.auction.room.dto.EnterAuctionRoomRequest;
import com.auction.room.repository.AuctionRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuctionRoomService {
    private final StringRedisTemplate redisTemplate;
    private final AuctionRoomRepository auctionRoomRepository;
    private final UserRepository userRepository;
    private List<Object> multiGet = List.of("itemName", "startPrice", "endPrice", "startTimestamp", "endTimestamp");

    @Transactional
    public ResponseEntity<AuctionRoom> addAuctionRoom(AddAuctionRoomRequest request) {
        // 경매방 등록자
        Optional<User> ownerOpt = userRepository.findById(request.userId());
        if (ownerOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        User user = ownerOpt.get();

        String uuid = UUID.randomUUID().toString();
        AuctionRoom auctionRoom = AuctionRoom.builder()
                .uuid(uuid)
                .startPrice(request.startPrice())
                .owner(user)
                .endPrice(request.startPrice())
                .startTimestamp(System.currentTimeMillis())
                .endTimestamp(request.endTimestamp())
                .itemName(request.itemName())
                .build();
        
        // AuctionRoom 의 유저리스트에 추가
        addAuctionRoom(uuid, user.getId());

        auctionRoomRepository.save(auctionRoom);
        redisTemplate.opsForHash().putAll(auctionRoom.toInfoKeyString(uuid), auctionRoom.toValueMap());
        return ResponseEntity.ok().body(auctionRoom);
    }


    public Set<String> enterAuctionRoom(EnterAuctionRoomRequest request, String roomId) {
        isExistAuctionRoom("auction:room:" + roomId);
        addAuctionRoom(roomId, request.userId());

        return redisTemplate.opsForSet()
                .members("auction:room:" + roomId);
    }

    /**
     * 방이 존재 하는지
     */
    private void isExistAuctionRoom(String key) {
        Long size = redisTemplate.opsForSet().size(key);

        // 선착순 처리 설정 50명
        // TODO : 방이 꽉찼을 때 예외처리, RuntimeException 이 아닌 예외를 만들거나 공통 처리를 해야함
        if (Objects.nonNull(size) && size < 50) {
            throw new RuntimeException();
        }
    }

    /**
     * AuctionRoom 유저 목록에 추가
     * @param roomId
     * @param userId
     */
    private void addAuctionRoom(String roomId, Long userId) {
        redisTemplate.opsForSet()
                .add("auction:room:" + roomId, userId.toString());
    }

    /**
     * 방 목록 반환
     * 방 id, 아이템 이름, 시작가, 시작/ 마감 시간, 인원
     */
    public ResponseEntity<List<AuctionRoomResponse>> findAuctionRoom() {
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

        return ResponseEntity.ok().body(result);
    }
}
package com.auction.room.controller;

import com.auction.room.domain.AuctionRoom;
import com.auction.room.dto.AddAuctionRoomRequest;
import com.auction.room.dto.EnterAuctionRoomRequest;
import com.auction.room.service.AuctionRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auction")
@RequiredArgsConstructor
public class AuctionRoomController {
    private final AuctionRoomService auctionRoomService;

    /**
     * TODO : 아직 응답 형식을 정하지 않아, 다른 기능 구현을 위해 임시로 해놓음, Valid 도 구현
     * 방 생성
     */
    @PostMapping
    public ResponseEntity<AuctionRoom> addAuctionRoom(@RequestBody AddAuctionRoomRequest request) {
        return auctionRoomService.addAuctionRoom(request);
    }

    /**
     * 방 입장, 방 유저 리스트 반환
     */
    @PostMapping("/{roomId}")
    public ResponseEntity<Set<String>> enterAuctionRoom(@RequestBody EnterAuctionRoomRequest request,
                                                        @PathVariable("roomId") String roomId) {
        return auctionRoomService.enterAuctionRoom(request, roomId);
    }
}

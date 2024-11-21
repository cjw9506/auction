package com.auction.room.dto;

public record EnterAuctionRoomRequest(
        Long userId
) {
    public static EnterAuctionRoomRequest of(Long userId) {
        return new EnterAuctionRoomRequest(userId);
    }
}
package com.auction.room.dto;

public record AddAuctionRoomRequest(
        Long userId,
        String itemName,
        Double startPrice
) {
}

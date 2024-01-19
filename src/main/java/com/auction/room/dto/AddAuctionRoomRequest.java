package com.auction.room.dto;

public record AddAuctionRoomRequest(
        String itemName,
        Double startPrice,
        Long endTimestamp
) {
}

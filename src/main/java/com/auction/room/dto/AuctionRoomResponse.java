package com.auction.room.dto;

public record AuctionRoomResponse(
        String uuid,
        String itemName,
        String startPrice,
        String endPrice,
        String startTimestamp,
        String endTimestamp,
        String numberOfPeople
) {
    public static AuctionRoomResponse of(String uuid, String itemName, String startPrice, String endPrice, String startTimestamp, String endTimestamp, String numberOfPeople) {
        return new AuctionRoomResponse(uuid, itemName, startPrice, endPrice, startTimestamp, endTimestamp, numberOfPeople);
    }
}

package com.auction.room.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuctionRoom {
    @Id
    private String uuid;
    private String itemName;

    // TODO : 유저 엔티티가 만들어 진다면, 변경해야함,
    private Long ownerId;
    private Long highestBidUserId;

    private Double startPrice;
    private Double endPrice;
    private Long startTimestamp;
    private Long endTimestamp;

    private boolean isActiveRoom = true;

    public void changeRoomStatus(boolean activeRoom) {
        isActiveRoom = activeRoom;
    }

    public String toInfoKeyString(String uuid) {
        return "auction:room:" + uuid + ":info";
    }
    public Map<String, String> toValueMap() {
        Map<String, String> valueMap = new HashMap<>();
        valueMap.put("itemName", itemName);
        valueMap.put("startPrice", startPrice.toString());
        valueMap.put("endPrice", endPrice.toString());
        valueMap.put("highestBidUserId", Objects.nonNull(highestBidUserId) ? highestBidUserId.toString() : null);
        valueMap.put("startTimestamp", startTimestamp.toString());
        valueMap.put("endTimestamp", endTimestamp.toString());
        return valueMap;
    }

    @Builder
    public AuctionRoom(String uuid, String itemName, Long highestBidUserId,
                       Double startPrice, Double endPrice, Long startTimestamp,
                       Long endTimestamp) {
        this.uuid = uuid;
        this.itemName = itemName;
        this.highestBidUserId = highestBidUserId;
        this.startPrice = startPrice;
        this.endPrice = endPrice;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }
}
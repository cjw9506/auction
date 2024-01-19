package com.auction.room.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuctionRoom {
    @Id
    private UUID uuid;
    private String itemName;

    // TODO : 유저 엔티티가 만들어 진다면, 변경해야함,
    private Long ownerId;
    private Long highestBidUserId;

    private Double startPrice;
    private Double endPrice;
    private Long startTimestamp;
    private Long endTimestamp;

    public String toKeyString() {
        return "auction:room:" + uuid.toString();
    }
    public Map<String, String> toValueMap() {
        Map<String, String> valueMap = new HashMap<>();
        valueMap.put("startPrice", startPrice.toString());
        valueMap.put("endPrice", endPrice.toString());
        valueMap.put("highestBidUserId", highestBidUserId.toString());
        valueMap.put("startTimestamp", startTimestamp.toString());
        valueMap.put("endTimestamp", endTimestamp.toString());
        return valueMap;
    }

    @Builder
    public AuctionRoom(String itemName, Double startPrice, Long endTimestamp) {
        this.uuid = UUID.randomUUID();
        this.itemName = itemName;
        this.highestBidUserId = 0L;
        this.startPrice = startPrice;
        this.endPrice = startPrice;
        this.startTimestamp = System.currentTimeMillis();
        this.endTimestamp = endTimestamp;
    }
}
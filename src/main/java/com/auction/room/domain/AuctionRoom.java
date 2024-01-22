package com.auction.room.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AuctionRoom that = (AuctionRoom) o;
        return getUuid() != null && Objects.equals(getUuid(), that.getUuid());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
package com.auction.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageDTO {

    private String roomId;
    private Long sender;
    private Double price;
    private boolean isHighest;

    public void updateStatus(boolean highest) {
        isHighest = highest;
    }
}

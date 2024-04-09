package com.auction.chat.dto;

import com.auction.chat.domain.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageDTO {

    private String roomId;
    private String sender;
    private String content;
    private MessageType type;
    private boolean isHighest;

    public void updateStatus(boolean highest) {
        isHighest = highest;
    }

    @Builder
    public MessageDTO(String roomId, String sender, String content, MessageType type) {
        this.roomId = roomId;
        this.sender = sender;
        this.content = content;
        this.type = type;
    }
}

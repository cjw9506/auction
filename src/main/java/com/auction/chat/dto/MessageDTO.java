package com.auction.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class MessageDTO {

    private Long roomId;
    private String sender;
    private String content;



}

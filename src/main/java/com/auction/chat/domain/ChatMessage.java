package com.auction.chat.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sender;

    private Double price;

    //TODO chatRoom 연겷하기

    @Builder
    public ChatMessage(Long sender, Double price) {
        this.sender = sender;
        this.price = price;
        //TODO ChatRoom 연결하기
    }
}

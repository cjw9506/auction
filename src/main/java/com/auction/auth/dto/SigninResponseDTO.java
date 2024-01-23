package com.auction.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SigninResponseDTO {

    private String token;

    @Builder
    public SigninResponseDTO(String token) {
        this.token = token;
    }
}

package com.auction.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class SigninDTO {

    @Length(min = 6, message = "계정은 최소 6글자입니다.")
    private String account;

    @Length(min = 6, message = "비밀번호는 최소 6글자입니다.")
    private String password;
}

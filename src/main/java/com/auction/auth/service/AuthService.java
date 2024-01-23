package com.auction.auth.service;

import com.auction.auth.domain.User;
import com.auction.auth.dto.SigninDTO;
import com.auction.auth.dto.SigninResponseDTO;
import com.auction.auth.dto.SignupDTO;
import com.auction.auth.jwt.JwtUtils;
import com.auction.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void userSignup(SignupDTO request) {

        //TODO 예외처리

        userRepository.save(User.builder()
                .account(request.getAccount())
                .password(passwordEncoder.encode(request.getPassword()))
                .build());
    }

    public SigninResponseDTO userSignin(SigninDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getAccount(),
                        request.getPassword()
                )
        );

        //TODO 예외처리
        User user = userRepository.findByAccount(request.getAccount()).orElseThrow(
                () -> new IllegalArgumentException("인증 실패"));

        String accessToken = jwtUtils.generateAccessToken(user);

        return SigninResponseDTO.builder()
                .token(accessToken)
                .build();

    }
}

package com.auction.auth.controller;

import com.auction.auth.dto.SigninDTO;
import com.auction.auth.dto.SigninResponseDTO;
import com.auction.auth.dto.SignupDTO;
import com.auction.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupDTO request ) {

        authService.userSignup(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping("/api/signin")
    public ResponseEntity<?> signin(@RequestBody @Valid SigninDTO request) {

        SigninResponseDTO response = authService.userSignin(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

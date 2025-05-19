package com.seven_eleven.haekathon.controller;

import com.seven_eleven.haekathon.dto.LoginRequest;
import com.seven_eleven.haekathon.dto.LoginResponse;
import com.seven_eleven.haekathon.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    // 로그인 요청을 처리하는 메서드
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}

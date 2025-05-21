package com.seven_eleven.haekathon.controller;

import com.seven_eleven.haekathon.dto.SignupRequestDto;
import com.seven_eleven.haekathon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    // 회원가입
    // @Valid 어노테이션을 사용하여 DTO의 유효성 검사를 수행
    // @RequestPart 어노테이션을 사용하여 multipart/form-data 형식으로 요청을 처리
    @PostMapping(value = "/signup", consumes = {"multipart/form-data"})
    public ResponseEntity<String> signup(
            @RequestPart("user") SignupRequestDto dto,
            @RequestPart(value = "photo", required = false) MultipartFile photo){
        userService.signup(dto, photo);
        return ResponseEntity.ok("회원가입 완료!");
    }

}

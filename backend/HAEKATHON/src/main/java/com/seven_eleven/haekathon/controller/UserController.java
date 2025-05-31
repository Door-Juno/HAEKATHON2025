package com.seven_eleven.haekathon.controller;

import com.seven_eleven.haekathon.domain.User;
import com.seven_eleven.haekathon.dto.SignupRequestDto;
import com.seven_eleven.haekathon.dto.UserResponseDto;
import com.seven_eleven.haekathon.repository.UserRepository;
import com.seven_eleven.haekathon.security.JwtUtill;
import com.seven_eleven.haekathon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final JwtUtill jwtUtill;
    private final UserRepository userRepository;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@ModelAttribute SignupRequestDto dto){
        System.out.println("🚀 [UserController] 회원가입 요청 도달");
        userService.signup(dto);
        return ResponseEntity.ok("회원가입 완료!");
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 나를 제외한 유저 목록 조회
    @GetMapping("/users/exclude-me")
    public ResponseEntity<List<UserResponseDto>> getUsersExceptMe(@RequestHeader("Authorization") String token) {
        Long CurrentUserId = jwtUtill.getUserIdFromToken(token.replace("Bearer ", ""));
        List<UserResponseDto> users = userService.getAllUsersNotMe(CurrentUserId);
        return ResponseEntity.ok(users);
    }

    //로그인 한 유저의 정보 불러오기
    @GetMapping("/users/me")
    public ResponseEntity<UserResponseDto> getUser(@RequestHeader("Authorization") String token) {
        Long CurrentUserId = jwtUtill.getUserIdFromToken(token.replace("Bearer ", ""));
        UserResponseDto user = userService.getUserById(CurrentUserId);
        return ResponseEntity.ok(user);
    }

    //특정 id의 정보 불러오기
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    //특정 id의 정보 삭제하기
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        UserResponseDto user = userService.deleteUserById(id);
        return ResponseEntity.ok("delete");
    }
}

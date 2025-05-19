package com.seven_eleven.haekathon.service;

import com.seven_eleven.haekathon.dto.LoginRequest;
import com.seven_eleven.haekathon.dto.LoginResponse;
import com.seven_eleven.haekathon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.seven_eleven.haekathon.security.JwtUtill;
import com.seven_eleven.haekathon.domain.User;

@Service
@RequiredArgsConstructor
public class AuthService {
    // 로그인 처리하는 서비스
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtill jwtUtil;

    public LoginResponse login(LoginRequest request){
        // 사용자 아이디로 사용자 정보 조회
        // 아이디가 존재하지 않으면 예외 발생
        User user = userRepository.findByUserid(request.getUserid())
                .orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다."));
        // 비밀번호가 일치하지 않으면 예외 발생
        // 비밀번호는 암호화되어 저장되므로, 입력된 비밀번호를 암호화하여 비교
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        // JWT 토큰 생성
        String token = jwtUtil.generateToken(user);
        return new LoginResponse(token, user.getId(),user.getName());
    }
}

package com.seven_eleven.haekathon.service;

import com.seven_eleven.haekathon.domain.User;
import com.seven_eleven.haekathon.dto.SignupRequestDto;
import com.seven_eleven.haekathon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public void signup(SignupRequestDto dto){
        // 사용자 이름 중복 제거
        if(userRepository.existsByUserid(dto.getUserid())){
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        User user = User.builder()
                .userid(dto.getUserid())
                // 비밀번호 암호화
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .major(dto.getMajor())
                .studentId(dto.getStudentId())
                .grade(dto.getGrade())
                .gender(dto.getGender())
                .description(dto.getDescription())
                .photoUrl(dto.getPhotoUrl())
                .build();
        userRepository.save(user);
    }
}

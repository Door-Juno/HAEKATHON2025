package com.seven_eleven.haekathon.service;

import com.seven_eleven.haekathon.domain.User;
import com.seven_eleven.haekathon.dto.SignupRequestDto;
import com.seven_eleven.haekathon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public void signup(SignupRequestDto dto , MultipartFile photo){
        // 사용자 이름 중복 제거
        if(userRepository.existsByUserid(dto.getUserid())){
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }
        //사진 저장 처리
        String photoUrl = null;
        try {
            if(photo != null && !photo.isEmpty()){
                // 사진 저장 로직
                String filename = UUID.randomUUID() + "_" + photo.getOriginalFilename();
                Path savePath = Paths.get("uploads", filename);
                Files.createDirectories(savePath.getParent());// 디렉토리가 없으면 생성한다
                photo.transferTo(savePath.toFile()); // 파일 저장
                photoUrl = "/uploads/" + filename; // 저장된 파일의 URL 사용자에게 노출된다.
            }
        }
        catch (Exception e) {
            throw new RuntimeException("사진 저장에 실패했습니다.", e);
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
                .photoUrl(photoUrl) // 사진 URL
                .build();
        userRepository.save(user);
    }
}


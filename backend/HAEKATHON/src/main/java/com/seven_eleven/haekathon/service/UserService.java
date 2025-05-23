package com.seven_eleven.haekathon.service;

import com.seven_eleven.haekathon.domain.User;
import com.seven_eleven.haekathon.dto.SignupRequestDto;
import com.seven_eleven.haekathon.dto.UserResponseDto;
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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public void signup(SignupRequestDto dto) {
        String photoPath = null;

        if (dto.getPhotoUrl() != null && !dto.getPhotoUrl().isEmpty()) {
            // 예시: uploads 디렉토리에 파일 저장
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            String fileName = UUID.randomUUID() + "_" + dto.getPhotoUrl().getOriginalFilename();

            File dest = new File(uploadDir + fileName);
            dest.getParentFile().mkdirs();
            try {
                dto.getPhotoUrl().transferTo(dest);
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 실패", e);
            }
            // DB에는 상대경로로 저장
            photoPath = "/uploads/" + fileName; // 저장된 파일의 경로
        }

        User user = User.builder()
                .userid(dto.getUserid())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .major(dto.getMajor())
                .studentId(dto.getStudentId())
                .grade(dto.getGrade())
                .gender(dto.getGender())
                .description(dto.getDescription())
                .photoUrl(photoPath)
                .build();

        userRepository.save(user);
    }


    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
    }
}


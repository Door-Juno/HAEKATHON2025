package com.seven_eleven.haekathon.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SignupRequestDto {
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String userid;
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
    private String name;
    private String major;
    private String studentId;
    private String grade;
    private String gender;
    private String description;
    private MultipartFile photoUrl;

}

package com.seven_eleven.haekathon.dto;

import com.seven_eleven.haekathon.domain.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;
    private String name;
    private String userId;
    private String major;
    private String studentId;
    private String grade;
    private String gender;
    private String description;
    private String photoUrl;

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .userId(user.getUserid())
                .major(user.getMajor())
                .studentId(user.getStudentId())
                .grade(user.getGrade())
                .gender(user.getGender())
                .description(user.getDescription())
                .photoUrl(user.getPhotoUrl())
                .build();
    }
}


package com.seven_eleven.haekathon.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 중복 불가
    @Column(nullable = false, unique = true)
    private String userid;

    @Column(nullable = false)
    private String password;

    // 이름
    @Column(nullable = false)
    private String name;

    // 전공
    private String major;

    // 학번 (앞 4자리만)
    private  String studentId;

    // 학년
    private String grade;

    // 성별
    private String gender;

    // 자기소개
    private String description;

    // 프로필 사진
    private String photoUrl;

    // 회원 가입 시간
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}

package com.seven_eleven.haekathon.repository;

import com.seven_eleven.haekathon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 사용자 이름으로 User 객체를 찾는 메서드
    boolean existsByUserid(String userid);
    Optional<User> findByUserid(String userid);
}
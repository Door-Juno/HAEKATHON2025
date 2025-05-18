package com.seven_eleven.haekathon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    // Spring Security 설정을 위한 클래스
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 비밀번호 암호화를 위한 PasswordEncoder Bean 등록
        return new BCryptPasswordEncoder();
    }

    // HttpSecurity 설정
    // Signup과 login 요청에 대한 CSRF 보호를 비활성화
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/signup",
                                "/api/login",
                                "**/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll() // 회원가입과 로그인 요청 허용
                        .anyRequest().authenticated() // 그 외의 요청은 인증 필요
                ).httpBasic(Customizer.withDefaults());
        return http.build();
    }
}

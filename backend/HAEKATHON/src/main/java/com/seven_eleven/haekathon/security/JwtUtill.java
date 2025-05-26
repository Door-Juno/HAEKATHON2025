package com.seven_eleven.haekathon.security;

import com.seven_eleven.haekathon.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtill {
    // JWT 비밀키와 만료시간 설정
    // 비밀키는 절대 외부에 노출되면 안됨
    // 만료시간은 1시간으로 설정
    // 비밀키는 256비트 이상이어야 함 (long 도배한 이유)
    private final String SECRET = "haekathon_super_secure_secret_key_long_long_long_long_long_long_long_long_long_long_long";
    private final long EXPIRATION = 1000 * 60 * 60 ;// 1시간

    // JWT 생성에 사용할 비밀키
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // JWT 생성
    public String generateToken(User user){
        return Jwts.builder()
                .setSubject(user.getUserid())
                .claim("userId", user.getId())
                .claim("name", user.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}

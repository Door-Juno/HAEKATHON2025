package com.seven_eleven.haekathon.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtill jwtUtil;

    public JwtAuthenticationFilter(JwtUtill jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 로그
        System.out.println("🔥 [JwtFilter] 요청 URI = " + request.getRequestURI());

        String token = jwtUtil.resolveToken(request);

        // ✅ 토큰이 있는 경우에만 인증 설정, 없으면 통과!
        if (token != null && jwtUtil.validateToken(token)) {
            // 로그
            System.out.println("✅ [JwtFilter] 유효한 토큰: " + token);

            Authentication auth = jwtUtil.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        // 로그
        else {
            System.out.println("⚠️ [JwtFilter] 토큰 없음 or 무효");
        }

        // ✅ 무조건 다음 필터로 넘긴다
        filterChain.doFilter(request, response);
    }
}

package com.seven_eleven.haekathon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // WebMvcConfigurer를 구현하여 필요한 설정을 추가할 수 있습니다.
    // 예를 들어, CORS 설정, 뷰 리졸버 설정 등을 추가할 수 있습니다.
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 허용할 출처
                .allowedMethods("*") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 허용할 헤더
                .allowCredentials(true); // 자격 증명 허용 여부
    }
}


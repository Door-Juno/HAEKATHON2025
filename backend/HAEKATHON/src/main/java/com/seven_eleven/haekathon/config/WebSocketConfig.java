package com.seven_eleven.haekathon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*") // 모든 출처 허용
                .withSockJS(); // SockJS를 사용하여 WebSocket을 지원
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.setApplicationDestinationPrefixes("/pub"); // 클라이언트가 메시지를 보낼 때 사용할 접두사
        config.enableSimpleBroker("/sub"); // 클라이언트가 구독할 수 있는 엔드포인트
    }
}

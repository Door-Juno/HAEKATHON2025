package com.seven_eleven.haekathon.dto;

import com.seven_eleven.haekathon.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MessageResponseDto {
    private Long messageId;
    private Long senderId;
    private String senderName;
    private String content;
    private LocalDateTime timestamp; // 메시지 전송 시간
    private boolean isRead;

    public static MessageResponseDto from(Message message) {
        return MessageResponseDto.builder()
                .messageId(message.getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getName())
                .content(message.getContent())
                .timestamp(message.getTimestamp()) // 또는 createdAt
                .isRead(message.isRead())       // 여기 중요! read 값 반영 확인
                .build();
    }
}

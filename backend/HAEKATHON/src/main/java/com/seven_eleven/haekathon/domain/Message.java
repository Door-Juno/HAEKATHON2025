package com.seven_eleven.haekathon.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    // 메세지 소속 채팅방
    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    // 메세지 보낸 사용자
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    // 메세지 내용
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 메세지 전송 시간
    private LocalDateTime timestamp;

    // 메세지 읽음 여부
    private boolean isRead;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}

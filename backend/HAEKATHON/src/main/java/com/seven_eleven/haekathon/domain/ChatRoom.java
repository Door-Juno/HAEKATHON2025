package com.seven_eleven.haekathon.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "room_id")
    private Long roomId; // 채팅방 ID (자동 생성)
    // 대화하는 두 사용자
    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    // 대화방 식별자
    @Column(nullable = false, unique = true)
    private String roomKey;

}

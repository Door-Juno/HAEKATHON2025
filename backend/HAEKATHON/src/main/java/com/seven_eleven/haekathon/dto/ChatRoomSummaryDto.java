package com.seven_eleven.haekathon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomSummaryDto {

    private Long roomId;               // 채팅방 ID
    private Long opponentId;           // 상대방 ID
    private String opponentName;       // 상대방 이름
    private String opponentProfileUrl; // 상대방 프로필 사진 URL

    private String lastMessage;        // 마지막 메시지 내용
    private String lastMessageTime;    // 마지막 메시지 시간 (String or ISO DateTime format)

    private long unreadCount;          // 읽지 않은 메시지 수
}
// ChatRoomSummaryDto는 채팅방 목록을 보여줄 때 사용되는 DTO입니다.
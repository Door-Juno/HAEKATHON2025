package com.seven_eleven.haekathon.dto;

import com.seven_eleven.haekathon.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomResponseDto {
    private Long roomId;
    private Long user1Id;
    private String user1Name;
    private Long user2Id;
    private String user2Name;

    public static ChatRoomResponseDto from(ChatRoom chatRoom) {
        return ChatRoomResponseDto.builder()
                .roomId(chatRoom.getRoomId())
                .user1Id(chatRoom.getUser1().getId())
                .user1Name(chatRoom.getUser1().getName())
                .user2Id(chatRoom.getUser2().getId())
                .user2Name(chatRoom.getUser2().getName())
                .build();
    }
}

package com.seven_eleven.haekathon.controller;

import com.seven_eleven.haekathon.domain.ChatRoom;
import com.seven_eleven.haekathon.dto.ChatRoomResponseDto;
import com.seven_eleven.haekathon.dto.ChatRoomSummaryDto;
import com.seven_eleven.haekathon.security.JwtUtill;
import com.seven_eleven.haekathon.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final JwtUtill jwtUtill;

    // 채팅방 생성 or 조회
    @PostMapping
    public ResponseEntity<ChatRoom> createOrGetChatRoom(
            @RequestParam Long targetUserId,
            @RequestHeader("Authorization") String token
    ){
        Long myId = extractUserIdFromToken(token);
        ChatRoom chatRoom = chatRoomService.createChatRoom(myId, targetUserId);
        return ResponseEntity.ok(chatRoom);
    }

    // 내가 속한 채팅방 전체 조회 (원본 채팅방 객체 리스트)
    @GetMapping
    public ResponseEntity<List<ChatRoom>> getMyChatRooms(
            @RequestHeader("Authorization") String token
    ){
        Long myId = extractUserIdFromToken(token);
        return ResponseEntity.ok(chatRoomService.getChatRoomsForUser(myId));
    }

    // 내가 속한 채팅방 목록 요약 조회 (프론트 메인에 사용)
    @GetMapping("/summary")
    public ResponseEntity<List<ChatRoomSummaryDto>> getMyChatRoomSummaries(
            @RequestHeader("Authorization") String token
    ) {
        Long myId = extractUserIdFromToken(token);
        List<ChatRoomSummaryDto> summaries = chatRoomService.getChatRoomSummaries(myId);
        return ResponseEntity.ok(summaries);
    }

    // 특정 채팅방 단일 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomResponseDto> getChatRoomInfo(@PathVariable Long roomId){
        ChatRoom room = chatRoomService.getChatRoomById(roomId);
        return ResponseEntity.ok(ChatRoomResponseDto.from(room));
    }

    // 토큰에서 유저 ID 추출
    private Long extractUserIdFromToken(String token) {
        String rawToken = token.replace("Bearer ", "");
        return jwtUtill.getUserIdFromToken(rawToken);
    }
}

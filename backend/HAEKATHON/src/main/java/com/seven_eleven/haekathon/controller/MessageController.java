package com.seven_eleven.haekathon.controller;

import com.seven_eleven.haekathon.domain.Message;
import com.seven_eleven.haekathon.dto.MessageRequestDto;
import com.seven_eleven.haekathon.dto.MessageResponseDto;
import com.seven_eleven.haekathon.security.JwtUtill;
import com.seven_eleven.haekathon.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatrooms/{roomId}/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final JwtUtill jwtUtill;

    // 메시지 목록 (페이징)
    @GetMapping
    public ResponseEntity<Page<MessageResponseDto>> getMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        Page<Message> messages = messageService.getMessages(roomId, page, size);
        Page<MessageResponseDto> dtoPage = messages.map(MessageResponseDto::from);
        return ResponseEntity.ok(dtoPage);
    }

    // 메시지 전송
    @PostMapping
    public ResponseEntity<MessageResponseDto> sendMessage(
            @PathVariable Long roomId,
            @RequestBody @Valid MessageRequestDto requestDto,
            @RequestHeader("Authorization") String token
    ){
        Long userId = extractUserIdFromToken(token);
        Message message = messageService.sendMessage(userId, roomId, requestDto.getContent());
        return ResponseEntity.ok(MessageResponseDto.from(message));
    }

    // 읽음 처리
    @PostMapping("/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long roomId,
            @RequestHeader("Authorization") String token
    ){
        System.out.println("🔥 read 진입 완료 roomId = " + roomId);
        Long userId = extractUserIdFromToken(token);
        messageService.markMessagesAsRead(roomId, userId);
        return ResponseEntity.ok().build();
    }

    // 안 읽은 메시지 수 조회
    @GetMapping("/unread-count")
    public ResponseEntity<Long> countUnreadMessages(
            @PathVariable Long roomId,
            @RequestHeader("Authorization") String token
    ){
        Long userId = extractUserIdFromToken(token);
        long count = messageService.countUnreadMessages(roomId, userId);
        return ResponseEntity.ok(count);
    }

    private Long extractUserIdFromToken(String token) {
        String rawToken = token.replace("Bearer ", "");
        return jwtUtill.getUserIdFromToken(rawToken);
    }
}

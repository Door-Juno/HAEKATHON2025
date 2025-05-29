package com.seven_eleven.haekathon.controller;

import com.seven_eleven.haekathon.dto.ChatMessageDto;
import com.seven_eleven.haekathon.domain.Message;
import com.seven_eleven.haekathon.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatWebSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/message")
    public void handleMessage(ChatMessageDto chatMessageDto) {
        if (chatMessageDto.getSenderId() == null || chatMessageDto.getRoomId() == null) {
            System.err.println("❌ WebSocket 메시지에 필수 ID가 누락됨: " + chatMessageDto);
            return;
        }

        Message savedMessage = messageService.sendMessage(
                chatMessageDto.getSenderId(),
                chatMessageDto.getRoomId(),
                chatMessageDto.getMessage()
        );

        messagingTemplate.convertAndSend(
                "/sub/chat/room/" + chatMessageDto.getRoomId(),
                chatMessageDto
        );
    }
}


package com.seven_eleven.haekathon.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
    public enum MessageType {
        ENTER, TALK, LEAVE
    }

    private MessageType type;
    private Long roomId;
    private Long senderId;
    private String senderName;
    private String message;
}

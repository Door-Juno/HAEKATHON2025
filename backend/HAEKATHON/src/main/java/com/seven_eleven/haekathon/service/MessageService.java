package com.seven_eleven.haekathon.service;

import com.seven_eleven.haekathon.domain.ChatRoom;
import com.seven_eleven.haekathon.domain.Message;
import com.seven_eleven.haekathon.domain.User;
import com.seven_eleven.haekathon.repository.ChatRoomRepository;
import com.seven_eleven.haekathon.repository.MessageRepository;
import com.seven_eleven.haekathon.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 메세지 전송
    @Transactional
    public Message sendMessage (Long senderId, Long ChatRoomId, String content){
        User sender = userRepository.findById(senderId)
                .orElseThrow(()-> new IllegalArgumentException("보내는 사용자가 존재하지 않습니다."));

        ChatRoom chatRoom = chatRoomRepository.findById(ChatRoomId)
                .orElseThrow(()-> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        Message message = Message.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(content)
                .isRead(false)
                .build();
        return messageRepository.save(message);
    }

    // 채팅방 내 메세지 페이징 조회 (최신순으로)
    public Page<Message> getMessages(Long chatRoomId, int page, int size){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(()-> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        return messageRepository.findByChatRoomOrderByTimestampDesc(chatRoom, PageRequest.of(page,size));
    }

    // 읽음 처리
    @Transactional
    public void markMessagesAsRead(Long chatRoomId, Long userId){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        List<Message> unreadMessages = messageRepository.findByChatRoomAndIsReadFalse(chatRoom);

        for(Message msg : unreadMessages){
            if(!msg.getSender().getId().equals(userId)){
                msg.setRead(true);
            }
        }
    }

    // 채팅방에서 읽지 않은 메시지 수
    public long countUnreadMessages(Long chatRoomId, Long userId){
        return messageRepository.countByChatRoomIdAndIsReadFalseAndSenderIdNot(chatRoomId, userId);
    }


}

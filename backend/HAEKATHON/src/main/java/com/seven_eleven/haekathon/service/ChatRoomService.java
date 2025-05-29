package com.seven_eleven.haekathon.service;

import com.seven_eleven.haekathon.domain.ChatRoom;
import com.seven_eleven.haekathon.domain.Message;
import com.seven_eleven.haekathon.domain.User;
import com.seven_eleven.haekathon.dto.ChatRoomSummaryDto;
import com.seven_eleven.haekathon.repository.ChatRoomRepository;
import com.seven_eleven.haekathon.repository.MessageRepository;
import com.seven_eleven.haekathon.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    /**
     * 채팅방 생성 (중복 방지 - roomKey 기준)
     */
    @Transactional
    public ChatRoom createChatRoom(Long myId, Long targetUserId) {
        String roomKey = generateRoomKey(myId, targetUserId);
        Optional<ChatRoom> existing = chatRoomRepository.findByRoomKey(roomKey);

        if (existing.isPresent()) {
            return existing.get(); // 이미 존재하는 방
        }

        User user1 = userRepository.findById(myId).orElseThrow(() -> new IllegalArgumentException("내 정보 없음"));
        User user2 = userRepository.findById(targetUserId).orElseThrow(() -> new IllegalArgumentException("상대 정보 없음"));

        ChatRoom newRoom = ChatRoom.builder()
                .user1(user1)
                .user2(user2)
                .roomKey(roomKey)
                .build();

        return chatRoomRepository.save(newRoom);
    }

    /**
     * 내 채팅방 목록 조회
     */
    public List<ChatRoom> getChatRoomsForUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        return chatRoomRepository.findByUser1OrUser2(user, user);
    }

    /**
     * 채팅방 단일 조회
     */
    public ChatRoom getChatRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
    }

    /**
     * 요약 정보 조회
     */
    public List<ChatRoomSummaryDto> getChatRoomSummaries(Long myId) {
        User me = userRepository.findById(myId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        List<ChatRoom> rooms = chatRoomRepository.findByUser1OrUser2(me, me);

        return rooms.stream().map(room -> {
            User opponent = room.getUser1().getId().equals(myId) ? room.getUser2() : room.getUser1();

            Message lastMessage = messageRepository.findTopByChatRoomOrderByTimestampDesc(room)
                    .orElse(null);

            long unreadCount = messageRepository.findByChatRoomAndIsReadFalse(room).stream()
                    .filter(msg -> !msg.getSender().getId().equals(myId))
                    .count();

            return ChatRoomSummaryDto.builder()
                    .roomId(room.getRoomId())
                    .opponentId(opponent.getId())
                    .opponentName(opponent.getName())
                    .opponentProfileUrl(opponent.getPhotoUrl())
                    .lastMessage(lastMessage != null ? lastMessage.getContent() : "")
                    .lastMessageTime(lastMessage != null ? lastMessage.getTimestamp().toString() : "")
                    .unreadCount(unreadCount)
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * 항상 동일한 roomKey 생성 (id 순 정렬)
     */
    private String generateRoomKey(Long id1, Long id2) {
        return id1 < id2 ? id1 + "_" + id2 : id2 + "_" + id1;
    }
}

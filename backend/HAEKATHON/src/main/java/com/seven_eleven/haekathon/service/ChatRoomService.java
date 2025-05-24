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

    // 채팅방 생성
    @Transactional
    public ChatRoom createChatRoom(Long user1Id, Long user2Id){
        if(user1Id.equals(user2Id)) {
            throw new IllegalArgumentException("자기 자신과는 채팅할 수 없습니다.");
        }

        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new IllegalArgumentException("User1이 존재하지 않습니다."));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new IllegalArgumentException("User2가 존재하지 않습니다."));

        // 이미 존재하는 채팅방인지 확인
        Optional<ChatRoom> existing = chatRoomRepository.findByUser1AndUser2(user1, user2);
        if(existing.isPresent()){
            return existing.get();
        }
        existing = chatRoomRepository.findByUser2AndUser1(user2, user1);
        if(existing.isPresent()){
            return existing.get();
        }

        String roomkey = generateRoomKey(user1Id, user2Id);
        // 존재 하지 않는다면 새 채팅방 생성
        ChatRoom room = ChatRoom.builder()
                .user1(user1)
                .user2(user2)
                .roomKey(user1.getId() + "_" + user2.getId()) // roomKey는 user1과 user2의 ID를 조합하여 생성
                .build();
        return chatRoomRepository.save(room);
    }

    // 내가 속한 채팅방 리스트
    public List<ChatRoom> getChatRoomsForUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        return chatRoomRepository.findByUser1OrUser2(user, user);
    }

    // 채팅방 하나 조회
    public ChatRoom getChatRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
    }

    // 채팅방 요약 정보 리스트
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
    private String generateRoomKey(Long id1, Long id2) {
        return id1 < id2 ? id1 + "_" + id2 : id2 + "_" + id1;
    }
}

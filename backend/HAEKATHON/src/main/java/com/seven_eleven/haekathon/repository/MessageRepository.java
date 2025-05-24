package com.seven_eleven.haekathon.repository;

import com.seven_eleven.haekathon.domain.ChatRoom;
import com.seven_eleven.haekathon.domain.Message;
import com.seven_eleven.haekathon.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // 채팅방 내 메시지 페이징
    Page<Message> findByChatRoomOrderByTimestampDesc(ChatRoom chatRoom, Pageable pageable);

    // 채팅방에서 안 읽은 메시지를 읽음 처리
    List<Message> findByChatRoomAndIsReadFalse(ChatRoom chatRoom);

    long countByChatRoomIdAndIsReadFalseAndSenderIdNot(Long chatRoomId, Long senderId);

    // 가장 최근 메시지 하나 가져오기
    Optional<Message> findTopByChatRoomOrderByTimestampDesc(ChatRoom chatRoom);

}

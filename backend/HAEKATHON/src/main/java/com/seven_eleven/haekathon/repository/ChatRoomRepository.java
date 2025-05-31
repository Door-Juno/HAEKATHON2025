package com.seven_eleven.haekathon.repository;

import com.seven_eleven.haekathon.domain.ChatRoom;
import com.seven_eleven.haekathon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 두 유저 간의 채팅방이 이미 존재하는지 확인
    Optional<ChatRoom> findByUser1AndUser2(User user1, User user2);
    Optional<ChatRoom> findByUser2AndUser1(User user2, User user1);

    // 특정 유저가
    //    // 두 유저 간의 채팅방이 이미 존재하는지 확인
    //    Optional<ChatRoom> findByUser1AndUser2(User user1, User user2);
    //    Optional<ChatRoom> findByUser2AndUser1(User user2, User user1);
    //
    //    // 특정 유저가 참여한 모든 채팅방
    //    List<ChatRoom> findByUser1OrUser2(User user1, User user2);참여한 모든 채팅방
    List<ChatRoom> findByUser1OrUser2(User user1, User user2);

    Optional<ChatRoom> findByRoomKey(String roomKey);

}

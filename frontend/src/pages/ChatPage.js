import React, { useEffect, useState, useContext, useRef } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import UserContext from '../UserContext';
import InputBox from '../components/inputBox';
import Button from '../components/Button';
import api from '../api/axios';
import './ChatPage.css';

export default function ChatPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const { username, userId } = useContext(UserContext);

  const {
    roomId: initialRoomId,
    targetUserId,
    targetUserName,
    targetImage
  } = location.state || {};

  const [roomId, setRoomId] = useState(initialRoomId || null);
  const [messages, setMessages] = useState([]);
  const [message, setMessage] = useState('');
  const [chatRooms, setChatRooms] = useState([]);
  const [stompClient, setStompClient] = useState(null);
  const messagesEndRef = useRef(null);

  /*
  useEffect(() => {
    api.get('/api/chatrooms/summary')
        .then(res => {
          setChatRooms(res.data);
        })
        .catch(err => console.error("❌ 채팅방 목록 불러오기 실패:", err));
  }, []);
  */
  useEffect(() => {
    if (!targetUserId) return;

    api.post('/api/chatrooms', null, { params: { targetUserId } })
        .then(res => {
          const roomIdFromServer = res.data.roomId ?? res.data.id;
          if (roomIdFromServer) setRoomId(roomIdFromServer);
        })
        .catch(err => console.error("❌ 채팅방 생성 실패:", err));
  }, [targetUserId]);

  useEffect(() => {
    if (!roomId) return;

    const fetchMessagesAndMarkRead = async () => {
      try {
        const res = await api.get(`/api/chatrooms/${roomId}/messages`, {
          params: { page: 0, size: 50 }
        });
        setMessages(res.data.content);
        await api.post(`/api/chatrooms/${roomId}/messages/read`);
      } catch (err) {
        console.error("❌ 메시지 로딩 실패:", err);
      }
    };

    fetchMessagesAndMarkRead();
  }, [roomId]);

  useEffect(() => {
    if (!roomId) return;

    const socket = new SockJS('http://localhost:8080/ws-chat');
    const client = new Client({
      webSocketFactory: () => socket,
      onConnect: () => {
        client.subscribe(`/sub/chat/room/${roomId}`, (msg) => {
          const newMessage = JSON.parse(msg.body);
          setMessages(prev => [...prev, {
            ...newMessage,
            content : newMessage.message,
            timestamp: new Date(),
            isRead: true
          }]);
        });
        setStompClient(client);
      },
    });

    client.activate();

    return () => {
      if (client) {
        client.deactivate();
      }
    };
  }, [roomId]);

  useEffect(() => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }, [messages]);

  const handleSend = async () => {
    if (!roomId || !userId || !message.trim()) {
      console.warn("🚨 WebSocket 메시지 전송 실패: 필드 누락", { roomId, userId, message });
      return;
    }

    const payload = {
      type: "TALK",
      roomId,
      senderId: userId,
      senderName: username,
      message
    };

    try {
      if (stompClient) {
        stompClient.publish({
          destination: "/pub/chat/message",
          body: JSON.stringify(payload),
        });
      }
      // API 방식 메세지 보내기
      //await api.post(`/api/chatrooms/${roomId}/messages`, { content: message });
      setMessage('');
    } catch (err) {
      console.error('❌ 메시지 전송 실패', err);
    }
  };


  return (
      <div className="chat-container">
        <div className="chat-list">
          <h3> 채팅목록</h3>
          <div className = "chat-list-placeholder"> 아직 채팅목록이 없습니다.</div>
        </div>
        <div className="chat-main">
          <div className="chat-header">
            <div className="chat-back" onClick={() => navigate('/board')}>← 뒤로가기</div>
            <h2 className="chat-title">{targetUserName}님과의 채팅</h2>
          </div>

          <div className="chat-user">
            <img src={targetImage || 'https://placehold.co/100'} alt="상대 프로필" className="chat-profile" />
            <div className="chat-username">{targetUserName}</div>
          </div>

          <div className="chat-messages">
            {[...messages]
                .sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp))
                .map((msg, index) => (
                    <div
                        key={msg.messageId ?? `msg-${msg.senderId}-${msg.timestamp ?? index}`}
                        className={`chat-message ${msg.senderName === username ? 'me' : 'other'}`}
                    >
                      <strong>{msg.senderName}:</strong> {msg.content}
                    </div>
                ))}
            <div ref={messagesEndRef} />
          </div>

          <div className="chat-input-area">
            <InputBox
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                placeholder="메시지를 입력하세요"
            />
            <Button label="전송" onClick={handleSend} />
          </div>
        </div>
      </div>
  );
}

// src/pages/ChatPage.js
import React, { useEffect, useState, useContext, useRef } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import UserContext from '../UserContext';
import InputBox from '../components/inputBox';
import Button from '../components/Button';
import api from '../api/axios';
import './ChatPage.css';

export default function ChatPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const { username } = useContext(UserContext);

  const {
    roomId: initialRoomId,
    targetUserId,
    targetUserName,
    targetImage
  } = location.state || {};

  const [roomId, setRoomId] = useState(initialRoomId || null);
  const [messages, setMessages] = useState([]);
  const [message, setMessage] = useState('');
  const messagesEndRef = useRef(null);

  // 채팅방 생성 or 조회
  useEffect(() => {
    if (!targetUserId) return;

    api.post('/api/chatrooms', null, {
      params: { targetUserId }
    }).then(res => {
      const roomIdFromServer = res.data.roomId ?? res.data.id;
      if (roomIdFromServer) {
        setRoomId(roomIdFromServer);
      } else {
        console.warn("❌ roomId 없음", res.data);
      }
    }).catch(err => {
      console.error("❌ 채팅방 생성 실패:", err);
    });
  }, [targetUserId]);

  // 메시지 불러오기 + 읽음 처리 (polling 포함)
  useEffect(() => {
    if (!roomId) return;

    const fetchMessagesAndMarkRead = async () => {
      try {
        const res = await api.get(`/api/chatrooms/${roomId}/messages`, {
          params: { page: 0, size: 50 }
        });
        setMessages(res.data.content);

        await api.post(`/api/chatrooms/${roomId}/messages/read`);
        console.log("✅ 읽음 처리 완료");
      } catch (err) {
        console.error("❌ 메시지 갱신 또는 읽음 처리 실패:", err);
      }
    };

    fetchMessagesAndMarkRead();
    const interval = setInterval(fetchMessagesAndMarkRead, 5000);
    return () => clearInterval(interval);
  }, [roomId]);

  // 자동 스크롤 아래로
  useEffect(() => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }, [messages]);

  // 메시지 전송
  const handleSend = async () => {
    if (!roomId || !message.trim()) {
      console.warn('roomId 없음 또는 빈 메세지');
      return;
    }
    try {
      const res = await api.post(`/api/chatrooms/${roomId}/messages`, {
        content: message
      });
      setMessages(prev => [...prev, res.data]);
      setMessage('');
    } catch (err) {
      console.error('❌ 메세지 전송 실패', err);
    }
  };

  return (
      <div className="chat-container">
        <div className="chat-list">
          <h3>채팅 목록</h3>
          <div className="chat-list-placeholder">← 목록은 추후 구현 예정</div>
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
                .map((msg) => (
                    <div key={msg.messageId} className={`chat-message ${msg.senderName === username ? 'me' : 'other'}`}>
                      <strong>{msg.senderName}:</strong> {msg.content}
                      <div className="chat-meta">
                        {msg.isRead ? '✔✔ 읽음' : '✔ 안읽음'} | {new Date(msg.timestamp).toLocaleTimeString()}
                      </div>
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

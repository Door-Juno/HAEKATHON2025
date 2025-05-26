// src/pages/ChatPage.js
import React, { useEffect, useState, useContext } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import UserContext from '../UserContext';
import InputBox from '../components/inputBox';
import Button from '../components/Button';
import './ChatPage.css';

export default function ChatPage() {
  const navigate = useNavigate();
  const { username } = useContext(UserContext);
  const location = useLocation();
  const { targetUser } = location.state || {};
  const [profileImage, setProfileImage] = useState('');
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    if (targetUser) {
      const savedUsers = JSON.parse(localStorage.getItem('users')) || [];
      const targetData = savedUsers.find((user) => user.username === targetUser);
      if (targetData && targetData.image) {
        setProfileImage(targetData.image);
      } else {
        setProfileImage('https://placehold.co/100'); // 기본 이미지
      }
    }
  }, [targetUser]);

  const handleSend = () => {
    if (!message) return;
    const newMsg = { user: username, text: message };
    setMessages([...messages, newMsg]);
    setMessage('');
  };

  return (
    <div className="chat-container">
      {/* 왼쪽: 채팅 목록 (비워둠) */}
      <div className="chat-list">
        <h3>채팅 목록</h3>
        <div className="chat-list-placeholder">아직 채팅 목록이 없습니다.</div>
      </div>

      {/* 오른쪽: 채팅창 */}
      <div className="chat-main">
        <div className="chat-header">
          <div className="chat-back" onClick={() => navigate('/board')}>← 뒤로가기</div>
          <h2 className="chat-title">{targetUser ? `${targetUser}와의 채팅` : '채팅'}</h2>

        </div>

        <div className="chat-user">
          <img src={profileImage} alt="상대 프로필" className="chat-profile" />
          <div className="chat-username">{targetUser}</div>
        </div>

        <div className="chat-messages">
          {messages.length === 0 ? (
            <div className="chat-placeholder">아직 메시지가 없습니다.</div>
          ) : (
            messages.map((msg, i) => (
              <div key={i} className={`chat-message ${msg.user === username ? 'me' : 'other'}`}>
                <strong>{msg.user}:</strong> {msg.text}
              </div>
            ))
          )}
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

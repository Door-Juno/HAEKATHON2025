//BoardPage.jsx
// src/pages/BoardPage.jsx
import React, { useEffect, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import UserContext from '../UserContext';
import UserCard from '../components/UserCard';
import './BoardPage.css';

export default function BoardPage() {
  const navigate = useNavigate();
  const { username } = useContext(UserContext);
  const [users, setUsers] = useState([]);

  useEffect(() => {
    const savedUsers = JSON.parse(localStorage.getItem('users')) || [];
    setUsers(savedUsers);
  }, []);

  // ✅ 유효한 사용자만 필터링
  const validUsers = users.filter(
    (user) =>
      user && user.username && user.userId && user.major && user.studentId
  );

  return (
    <div className="board-wrapper">
      {/* 상단 유저 정보 + 로그아웃 */}
      <div className="board-header">
        <div className="board-user">{username}님 안녕하세요~</div>
        <div className="board-logout" onClick={() => navigate('/')}>
          로그아웃
        </div>
      </div>

      {/* 페이지 제목 */}
      <h1 className="board-title">우리 학교 사람들</h1>

      {/* 유저 카드 리스트 */}
      <div className="board-content">
        {validUsers.map((user, index) => (
          <UserCard
            key={index}
            name={user.username}
            major={user.major}
            grade={user.grade}
            number={user.studentId}
            gender={user.gender}
            intro={user.intro}
            image={user.image || 'https://placehold.co/143'}
            onChat={() => navigate('/chat', {
              state: {
                targetUser: user.username,      // 이름
                targetImage: user.image         // 프로필 이미지
  }
})}

          />
        ))}
      </div>
    </div>
  );
}

// src/pages/BoardPage.jsx
import React, { useEffect, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import UserContext from '../UserContext';
import UserCard from '../components/UserCard';
import api from '../api/axios';
import './BoardPage.css';

export default function BoardPage() {
  const navigate = useNavigate();
  const { username } = useContext(UserContext);
  const [users, setUsers] = useState([]);

  // 페이지 로드 시 유저 목록 불러오기
  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await api.get('/api/users/exclude-me');
        setUsers(response.data);
      } catch (error) {
        console.error('유저 목록 불러오기 실패:', error);
      }
    };
    fetchUsers();
  }, []);

  // 유효한 사용자만 필터링
  const validUsers = users.filter(
    (user) =>
      user &&
      user.name &&
      user.userId &&
      user.major &&
      user.studentId
  );

  return (
    <div className="board-wrapper">
      {/* 상단 유저 정보 + 로그아웃 */}
      <div className="board-header">
        <div className="board-user">{username}님 안녕하세요~</div>
        <div
          className="board-logout"
          onClick={() => {
            localStorage.removeItem('token');
            localStorage.removeItem('username');
            navigate('/');
          }}
        >
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
            name={user.name}
            major={user.major}
            grade={user.grade + '학년'}
            number={user.studentId + '학번'}
            gender={user.gender}
            intro={user.description}
            image={
              user.photoUrl
                ? `http://localhost:8080${user.photoUrl}`
                : 'https://placehold.co/143'
            }
            onChat={() =>
              navigate('/chat', {
                state: {
                  targetUser: user.name,
                  targetImage: user.photoUrl
                    ? `http://localhost:8080${user.photoUrl}`
                    : 'https://placehold.co/143',
                },
              })
            }
          />
        ))}
      </div>
    </div>
  );
}


// src/pages/LoginPage.jsx
import React, { useState, useContext } from 'react';
import InputBox from '../components/inputBox';
import Button from '../components/Button';
import { useNavigate } from 'react-router-dom';
import UserContext from '../UserContext';
import api from '../api/axios';
import './LoginPage.css';

export default function LoginPage() {
  const [id, setId] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const { setUsername, setUserId } = useContext(UserContext);

  const handleLogin = async () => {
    try {
      const response = await api.post('/api/login', {
        userid: id,
        password: password,
      });

      console.log("✅ 로그인 응답:", response.data);

      const { token, userId, name } = response.data;

      localStorage.setItem('token', token);
      localStorage.setItem('username', name);
      localStorage.setItem('userId', userId);
      setUsername(name);
      setUserId(userId);

      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;

      alert(`${name}님 환영합니다!`);
      navigate('/board');
    } catch (error) {
      alert("로그인 실패: " + (error.response?.data?.message || "서버 오류"));
    }
  };

  return (
      <div className="login-page">
        <div className="login-wrapper">
          <h1 className="login-title">밥먹쟈</h1>
          <InputBox
              label="ID"
              value={id}
              onChange={(e) => setId(e.target.value)}
          />
          <InputBox
              label="Password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
          />
          <Button label="Login" onClick={handleLogin} />
          <div className="signup-link" onClick={() => navigate('/signup')}>
            Sign up
          </div>
        </div>
      </div>
  );
}

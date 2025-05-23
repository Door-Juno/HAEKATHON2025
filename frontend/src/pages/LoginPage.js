//LoginPage.jsx
// src/pages/LoginPage.jsx
import React, { useState, useContext } from 'react';
import InputBox from '../components/inputBox';
import Button from '../components/Button';
import { useNavigate } from 'react-router-dom';
import UserContext from '../UserContext';
import api from '../api/axios' ;
import './LoginPage.css';

export default function LoginPage() {
  const [id, setId] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const { setUsername } = useContext(UserContext);

  // JWT 로그인 요청
  // login 요청을 보내고, 성공하면 JWT를 localstorage에 저장합니다.
  const handleLogin = async () => {
    try{
      // 로그인 요청
      // 저 경로로 POST 요청을 보내고, 성공하면 JWT를 localstorage에 저장합니다.
      const response = await api.post('/api/login', {
        userid: id,
        password: password,
      });
      // 로그인 성공 시
      const {token, username} = response.data;

      // 토큰 저장
      localStorage.setItem('token', token);
      localStorage.setItem('username', username);
      setUsername(username);

      // axios 인스턴스에 토큰 설정
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;


      alert(`${username}님 환영합니다!`);
      navigate('/board');
    }
    catch (error) {
      // 로그인 실패 시
      alert("로그인 실패 : " +(error.response?. data?.message || "서버오류"));
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

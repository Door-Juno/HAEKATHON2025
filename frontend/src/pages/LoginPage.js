//LoginPage.jsx
// src/pages/LoginPage.jsx
import React, { useState, useContext } from 'react';
import InputBox from '../components/inputBox';
import Button from '../components/Button';
import { useNavigate } from 'react-router-dom';
import UserContext from '../UserContext';
import './LoginPage.css';

export default function LoginPage() {
  const [id, setId] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const { setUsername } = useContext(UserContext);

  const handleLogin = () => {
    const users = JSON.parse(localStorage.getItem('users')) || [];

    const matchedUser = users.find(
      (user) => user.userId === id && user.password === password
    );

    if (matchedUser) {
      alert(`${matchedUser.username}님 환영합니다!`);
      setUsername(matchedUser.username); // context에 저장
      navigate('/board');
    } else {
      alert('ID 또는 비밀번호가 틀렸습니다.');
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

// src/App.jsx
import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import BoardPage from './pages/BoardPage';
// import ChatPage from './pages/ChatPage'; // 채팅 페이지가 있다면

import UserContext from './UserContext';

function App() {
  const [username, setUsername] = useState('');

  return (
    <UserContext.Provider value={{ username, setUsername }}>
      <Router>
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route path="/board" element={<BoardPage />} />
          {/* <Route path="/chat" element={<ChatPage />} /> */}
        </Routes>
      </Router>
    </UserContext.Provider>
  );
}

export default App;
// src/App.jsx
import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import BoardPage from './pages/BoardPage';
// import ChatPage from './pages/ChatPage';

import UserContext from './UserContext';
import PrivateRoute from './components/PrivateRoute';
import api from './api/axios';

function App() {
    const [username, setUsername] = useState('');

    // 앱 로드 시 토큰 복구
    useEffect(() => {
        const token = localStorage.getItem('token');
        const storedUsername = localStorage.getItem('username');
        if (token) {
            api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        }
        if (storedUsername) {
            setUsername(storedUsername);
        }
    }, []);

    return (
        <UserContext.Provider value={{ username, setUsername }}>
            <Router>
                <Routes>
                    <Route path="/" element={<LoginPage />} />
                    <Route path="/signup" element={<SignupPage />} />
                    <Route
                        path="/board"
                        element={
                            <PrivateRoute>
                                <BoardPage />
                            </PrivateRoute>
                        }
                    />
                    {/* <Route
            path="/chat"
            element={
              <PrivateRoute>
                <ChatPage />
              </PrivateRoute>
            }
          /> */}
                </Routes>
            </Router>
        </UserContext.Provider>
    );
}

export default App;

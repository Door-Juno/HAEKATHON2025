// src/App.jsx
import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import BoardPage from './pages/BoardPage';
import ChatPage from './pages/ChatPage';

import UserContext from './UserContext';
import PrivateRoute from './components/PrivateRoute';
import api from './api/axios';

function App() {
    const [username, setUsername] = useState('');
    const [userId, setUserId] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem('token');
        const storedUsername = localStorage.getItem('username');
        const storedUserId = localStorage.getItem('userId');
        if (token) {
            api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        }
        if (storedUsername) setUsername(storedUsername);
        if (storedUserId) setUserId(parseInt(storedUserId));
    }, []);

    return (
        <UserContext.Provider value={{ username, setUsername, userId, setUserId }}>
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
                    <Route
                        path="/chat"
                        element={
                            <PrivateRoute>
                                <ChatPage />
                            </PrivateRoute>
                        }
                    />
                </Routes>
            </Router>
        </UserContext.Provider>
    );
}

export default App;

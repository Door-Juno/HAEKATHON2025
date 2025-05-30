import React from 'react';
import { Navigate } from 'react-router-dom';

export default function PrivateRoute({ children }) {
    const token = localStorage.getItem('token');

    // 토큰이 없으면 로그인 페이지로 리다이렉트
    if (!token) {
        return <Navigate to="/" replace />;
    }

    // 토큰이 있으면 자식 컴포넌트를 렌더링
    return children;
}
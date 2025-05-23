/*
백엔드 스프링 서버랑 연결하는 역할입니다.
axios 라이브러리를 사용하여 HTTP 요청을 보내고 응답을 처리합니다.
 */

// src/api/axios.js
import axios from "axios";

const token = localStorage.getItem("token"); // localStorage에서 JWT 토큰을 가져옵니다.

const api = axios.create({
    baseURL: "http://localhost:8080", // 스프링 서버 주소
    withCredentials: true, // JWT 쿠키 등 인증 정보를 보낼 때 필요
    headers: {
        ...(token && { Authorization: `Bearer ${token}` }), // JWT 토큰을 헤더에 추가
    }
});

export default api;

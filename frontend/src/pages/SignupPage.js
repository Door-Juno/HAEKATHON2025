//SignupPage.jsx
// src/pages/SignupPage.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import InputBox from '../components/inputBox';
//import Button from '../components/Button';
import './SignupPage.css';

export default function SignupPage() {
  const [formData, setFormData] = useState({
    username: '',
    userId: '',
    password: '',
    major: '',
    studentId: '',
    grade: '',
    gender: '',
    intro: '',
    profileImage: null,
  });

  const [preview, setPreview] = useState(null);
  const navigate = useNavigate();

  const handleChange = (key) => (e) => {
    setFormData({ ...formData, [key]: e.target.value });
  };

  const handleSelect = (key, value) => {
    setFormData({ ...formData, [key]: value });
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setFormData({ ...formData, profileImage: file });
      setPreview(URL.createObjectURL(file));
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const existingUsers = JSON.parse(localStorage.getItem('users')) || [];

    const newUser = {
      ...formData,
      image: preview || '', // 이미지 경로 저장용 (지금은 임시)
    };

    const updatedUsers = [...existingUsers, newUser];
    localStorage.setItem('users', JSON.stringify(updatedUsers));

    alert('회원가입 완료!');
    navigate('/');
  };

  return (
    <div className="signup-page">
      <h2>회원가입</h2>
      <form className="signup-form" onSubmit={handleSubmit}>
        {/* 왼쪽 폼 */}
        <div className="form-left">
          <InputBox
            label="Username"
            value={formData.username}
            onChange={handleChange('username')}
          />
          <InputBox
            label="ID"
            value={formData.userId}
            onChange={handleChange('userId')}
          />
          <InputBox
            label="Password"
            type="password"
            value={formData.password}
            onChange={handleChange('password')}
          />
          <InputBox
            label="Major"
            value={formData.major}
            onChange={handleChange('major')}
          />
          <InputBox
            label="Student ID"
            value={formData.studentId}
            onChange={handleChange('studentId')}
          />

          <label>학년</label>
          <div className="grade-buttons">
            {[1, 2, 3, 4].map((num) => (
              <button
                key={num}
                type="button"
                className={formData.grade === String(num) ? 'selected' : ''}
                onClick={() => handleSelect('grade', String(num))}
              >
                {num}
              </button>
            ))}
          </div>
        </div>

        {/* 오른쪽 폼 */}
        <div className="form-right">
          <label>프로필 이미지</label>
          <input type="file" accept="image/*" onChange={handleImageChange} />
          {preview && (
            <img src={preview} alt="preview" className="profile-preview" />
          )}

          <label>성별</label>
          <div className="gender-buttons">
            {['남성', '여성'].map((g) => (
              <button
                key={g}
                type="button"
                className={formData.gender === g ? 'selected' : ''}
                onClick={() => handleSelect('gender', g)}
              >
                {g}
              </button>
            ))}
          </div>

          <label>자기소개</label>
          <textarea
            name="intro"
            value={formData.intro}
            onChange={handleChange('intro')}
            rows={5}
          />
        </div>

        <div className="form-submit">
          <button type="submit" className="signup-btn">
            Sign Up
          </button>
        </div>
      </form>
    </div>
  );
}
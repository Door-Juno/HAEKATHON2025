import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import InputBox from '../components/inputBox';
import './SignupPage.css';
import api from '../api/axios';

export default function SignupPage() {
  const [formData, setFormData] = useState({
    name: '',            // 이름
    userid: '',          // 아이디
    password: '',        // 비밀번호
    major: '',
    studentId: '',
    grade: '',
    gender: '',
    description: '',     // 자기소개
    photoUrl: null       // 이미지 파일
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
      setFormData({ ...formData, photoUrl: file });
      setPreview(URL.createObjectURL(file));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const form = new FormData();

      form.append('name', formData.name);
      form.append('userid', formData.userid); // ✅ 필드명 서버와 맞춤
      form.append('password', formData.password);
      form.append('major', formData.major);
      form.append('studentId', formData.studentId);
      form.append('grade', formData.grade);
      form.append('gender', formData.gender);
      form.append('description', formData.description);

      if(formData.photoUrl) {
        form.append('photoUrl', formData.photoUrl);
      }

      await api.post('/api/signup', form, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      alert('회원가입 성공');
      navigate('/');
    } catch (error) {
      alert('회원가입 실패 : ' + (error.response?.data?.message || '서버 오류'));
    }
  };

  return (
      <div className="signup-page">
        <h2>회원가입</h2>
        <form className="signup-form" onSubmit={handleSubmit}>
          {/* 왼쪽 */}
          <div className="form-left">
            <InputBox
                label="Username"
                value={formData.name}
                onChange={handleChange('name')}
            />
            <InputBox
                label="ID"
                value={formData.userid}
                onChange={handleChange('userid')}
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
                label="Student ID Prefix"
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

          {/* 오른쪽 */}
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

            <label>자기소개 100자 내외로 표현해주세요.</label>
            <textarea
                name="description"
                value={formData.description}
                onChange={handleChange('description')}
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

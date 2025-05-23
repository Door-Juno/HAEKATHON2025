//UserCard.jsx
// src/components/UserCard.jsx
import './UserCard.css';

export default function UserCard({
  name,
  major,
  grade,
  number,
  gender,
  intro,
  image,
  onChat,
}) {
  return (
    <div className="user-card">
      <img className="user-img" src={image} alt={`${name}의 프로필`} />
      <div className="user-info major">{major}</div>
      <div className="user-info grade">{grade}</div>
      <div className="user-info number">{number}</div>
      <div className="user-info gender">{gender}</div>
      <div className="user-intro">{intro}</div>
      <button className="message-button" onClick={onChat}>
        메시지 보내기
      </button>
    </div>
  );
}

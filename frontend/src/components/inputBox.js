//InputBox.jsx
// src/components/InputBox.jsx
import userIcon from '../assets/user.png';
import lockIcon from '../assets/lock.png';
import './InputBox.css';

export default function InputBox({
  label,
  icon, // 'user' 또는 'lock'
  type = 'text',
  value,
  onChange,
  placeholder,
}) {
  const iconSrc = icon === 'user' ? userIcon : lockIcon;

  return (
    <div className="input-box">
      {label && <label className="input-label">{label}</label>}
      <div style={{ display: 'flex', alignItems: 'center' }}>
        {icon && <img src={iconSrc} alt={icon} className="input-icon" />}
        <input
          className="input-field"
          type={type}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
        />
      </div>
    </div>
  );
}
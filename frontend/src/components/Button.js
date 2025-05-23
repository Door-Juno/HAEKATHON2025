// src/components/Button.jsx
import './Button.css';

export default function Button({
  label,
  onClick,
  active = false,
  className = '',
}) {
  return (
    <button
      className={`custom-button ${active ? 'active' : ''} ${className}`}
      onClick={onClick}
    >
      {label}
    </button>
  );
}
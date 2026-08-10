import React, { useContext, useRef, useEffect } from 'react';
import { ThemeContext } from '../context/ThemeContext';

const Account = ({ balance }) => {
  const { theme, toggleTheme } = useContext(ThemeContext);
  const headingRef = useRef(null);

  useEffect(() => {
    // small demo: add a title attribute to the heading for accessibility
    if (headingRef.current) headingRef.current.setAttribute('title', `Current balance is ₹${balance}`);
  }, [balance]);

  const containerStyle = {
    padding: 8,
    background: theme === 'dark' ? '#222' : '#fff',
    color: theme === 'dark' ? '#eaeaea' : '#111',
    borderRadius: 4,
    border: '1px solid #ddd'
  };

  return (
    <div style={containerStyle}>
      <h2 ref={headingRef}>💰 Current Balance: ₹{balance}</h2>
      {balance < 0 && <p style={{ color: 'red' }}>⚠️ Overdraft Alert!</p>}
      <button onClick={toggleTheme} style={{ marginTop: 8 }}>
        Toggle Theme ({theme})
      </button>
    </div>
  );
}

export default Account;
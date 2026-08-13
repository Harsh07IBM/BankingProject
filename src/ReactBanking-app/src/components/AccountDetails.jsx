import React, { useContext, useRef, useEffect, useState } from 'react';
import { ThemeContext } from '../context/ThemeContext';

const AccountDetails = ({ balance }) => {
  const { theme, toggleTheme } = useContext(ThemeContext);
  const headingRef = useRef(null);
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // small demo: add a title attribute to the heading for accessibility
    if (headingRef.current) headingRef.current.setAttribute('title', `Current balance is ₹${balance}`);
  }, [balance]);

  // Simulate a backend API call using fetch with JSONPlaceholder
  useEffect(() => {
    fetch('https://jsonplaceholder.typicode.com/users/1')
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        console.log('API Response:', data);
        setUserData(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error('Fetch failed:', err);
        setError(err.message);
        setLoading(false);
      });
  }, []); // runs once on mount

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

      {/* Data fetched from backend API */}
      <hr style={{ margin: '12px 0', borderColor: theme === 'dark' ? '#444' : '#ddd' }} />
      <h3 style={{ margin: '8px 0' }}>📡 Account Holder Info (via API)</h3>
      {loading && <p>Loading data from server...</p>}
      {error && <p style={{ color: 'red' }}>❌ Error: {error}</p>}
      {userData && (
        <div style={{ textAlign: 'left', padding: '0 12px', fontSize: 14 }}>
          <p><strong>Name:</strong> {userData.name}</p>
          <p><strong>Email:</strong> {userData.email}</p>
          <p><strong>Phone:</strong> {userData.phone}</p>
          <p><strong>Company:</strong> {userData.company?.name}</p>
          <p><strong>Website:</strong> {userData.website}</p>
        </div>
      )}

      <button onClick={toggleTheme} style={{ marginTop: 8 }}>
        Toggle Theme ({theme})
      </button>
    </div>
  );
}

export default AccountDetails;

// Step 3: BalanceController — component for withdraw and deposit
import React, { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';

function BalanceController() {
  const [amount, setAmount] = useState('');

  // Access balance from Redux store
  const balance = useSelector((state) => state.balance);

  // Get the dispatch function to dispatch actions
  const dispatch = useDispatch();

  const handleDeposit = () => {
    const value = parseFloat(amount);
    if (!value || value <= 0) return alert('Enter a valid amount');
    dispatch({ type: 'DEPOSIT', payload: value });
    setAmount('');
  };

  const handleWithdraw = () => {
    const value = parseFloat(amount);
    if (!value || value <= 0) return alert('Enter a valid amount');
    if (value > balance) return alert('Insufficient balance');
    dispatch({ type: 'WITHDRAW', payload: value });
    setAmount('');
  };

  return (
    <div style={{
      maxWidth: 420,
      margin: '30px auto',
      padding: 24,
      border: '1px solid #ccc',
      borderRadius: 8,
      background: 'rgba(33, 17, 17, 0.384)',
      textAlign: 'center',
      color: 'white',
    }}>
      <h2>💳 Balance Controller</h2>
      <p style={{ fontSize: 22, fontWeight: 'bold', color: '#4caf50' }}>
        ₹{balance.toFixed(2)}
      </p>

      <input
        type="number"
        placeholder="Enter amount (₹)"
        value={amount}
        onChange={(e) => setAmount(e.target.value)}
        style={{ padding: 8, width: 180, marginBottom: 12 }}
      />

      <div style={{ display: 'flex', gap: 10, justifyContent: 'center', marginTop: 8 }}>
        <button onClick={handleDeposit} style={{ padding: '8px 20px', cursor: 'pointer', background: '#4caf50', color: 'white', border: 'none', borderRadius: 4 }}>
          Deposit
        </button>
        <button onClick={handleWithdraw} style={{ padding: '8px 20px', cursor: 'pointer', background: '#f44336', color: 'white', border: 'none', borderRadius: 4 }}>
          Withdraw
        </button>
      </div>
    </div>
  );
}

export default BalanceController;

import { useState } from 'react';
import './TransactionForm.css';

function TransactionForm({ balance, onTransfer }) {
  const [toAccount, setToAccount] = useState('');
  const [amount, setAmount] = useState('');

  const handleTransfer = () => {
    const value = parseFloat(amount);

    if (toAccount.length !== 12 || isNaN(toAccount)) {
      return alert("Enter a valid 12-digit account number");
    }
    if (!value || value <= 0) {
      return alert("Enter a valid amount");
    }
    if (value > balance) {
      return alert("Insufficient balance");
    }

    onTransfer(toAccount, value);
    setToAccount('');
    setAmount('');
  };

  return (
    <div className="transaction-form">
      <h3>Transfer Money</h3>
      <input
        type="text"
        placeholder="12-digit Account No."
        value={toAccount}
        onChange={(e) => setToAccount(e.target.value)}
        maxLength={12}
      />
      <input
        type="number"
        placeholder="Amount (₹)"
        value={amount}
        onChange={(e) => setAmount(e.target.value)}
      />
      <button onClick={handleTransfer}>Send</button>
    </div>
  );
}

export default TransactionForm;

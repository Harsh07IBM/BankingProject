import '../components/Dashboard.css';
import React, { useEffect, useLayoutEffect, useInsertionEffect, useRef } from 'react';
import AccountDetails from '../components/AccountDetails';

function Home({ account, amount, setAmount, handleCredit, handleDebit }) {
  const balanceRef = useRef(null);
  const inputRef = useRef(null);

  // Run after paint: update document title when balance changes
  useEffect(() => {
    document.title = `Balance: ₹${account.balance}`;
  }, [account.balance]);

  // Measure the balance element before paint (layout) to avoid flicker
  useLayoutEffect(() => {
    if (balanceRef.current) {
      // simple measurement example
      const w = balanceRef.current.getBoundingClientRect().width;
      // attach a data attribute for debugging / learning
      balanceRef.current.setAttribute('data-width', Math.round(w));
    }
  }, [account.balance]);

  // Inject a tiny style before DOM updates (lightweight demo)
  useInsertionEffect(() => {
    const style = document.createElement('style');
    style.textContent = `.dashboard { padding: 12px; }`;
    document.head.appendChild(style);
    return () => document.head.removeChild(style);
  }, []);

  // focus the amount input on mount to show useRef usage
  useEffect(() => {
    if (inputRef.current) inputRef.current.focus();
  }, []);

  return (
    <div className="dashboard">
      <h1 style={{ color: 'black' }}>{account.bankName}</h1>
      <h2 className="description">Secure, Transparent Blockchain-Powered Digital Banking.</h2>
      <AccountDetails balance={account.balance} />
      <h3>Account Holder: {account.holderName}</h3>
      <p>Account Type: {account.accountType}</p>
      <p className="balance" ref={balanceRef}>Balance: ₹{account.balance}</p>

      <div className="transaction">
        <input
          ref={inputRef}
          type="number"
          placeholder="Enter amount"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
        />
        <button onClick={handleCredit}>Credit</button>
        <button onClick={handleDebit}>Debit</button>
      </div>
    </div>
  );
}

export default Home;

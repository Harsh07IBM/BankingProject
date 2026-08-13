// Step 2: Use useSelector and useDispatch from react-redux in App.js
import { useState } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import './App.css';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Accounts from './pages/Accounts';
import About from './pages/About';
import TransferFunds from './components/TransferFunds';
import Transactions from './components/Transactions';
import BalanceController from './components/BalanceController';
import { ThemeProvider } from './context/ThemeContext';

function App() {
  // Access balance from Redux store
  const balance = useSelector((state) => state.balance);

  // Dispatch actions to the Redux store
  const dispatch = useDispatch();

  const [account, setAccount] = useState({
    bankName: "Web3 Ledger Bank",
    holderName: "Harsh Kumar",
    accountType: "Savings",
    balance: 85250.75,
  });
  const [amount, setAmount] = useState('');
  const [history, setHistory] = useState([]);

  const handleCredit = () => {
    const value = parseFloat(amount);
    if (!value || value <= 0) return alert("Enter a valid amount");
    setAccount({ ...account, balance: account.balance + value });
    // Also dispatch to Redux store
    dispatch({ type: 'DEPOSIT', payload: value });
    setHistory([{ type: 'Credit', amount: value, date: new Date().toLocaleString() }, ...history]);
    setAmount('');
  };

  const handleDebit = () => {
    const value = parseFloat(amount);
    if (!value || value <= 0) return alert("Enter a valid amount");
    if (value > account.balance) return alert("Insufficient balance");
    setAccount({ ...account, balance: account.balance - value });
    // Also dispatch to Redux store
    dispatch({ type: 'WITHDRAW', payload: value });
    setHistory([{ type: 'Debit', amount: value, date: new Date().toLocaleString() }, ...history]);
    setAmount('');
  };

  const handleTransfer = (toAccount, value) => {
    setAccount({ ...account, balance: account.balance - value });
    // Also dispatch to Redux store
    dispatch({ type: 'WITHDRAW', payload: value });
    setHistory([
      { type: 'Transfer', amount: value, to: toAccount, date: new Date().toLocaleString() },
      ...history,
    ]);
  };

  return (
    <BrowserRouter>
      <ThemeProvider>
        <Navbar />
        {/* Display Redux store balance at the top */}
        <div style={{ textAlign: 'center', padding: '8px', background: '#1a1a2e', color: '#4caf50', fontSize: 14 }}>
          🏦 Redux Store Balance: ₹{balance.toFixed(2)}
        </div>
        <Routes>
          <Route
            path="/"
            element={
              <Home
                account={account}
                amount={amount}
                setAmount={setAmount}
                handleCredit={handleCredit}
                handleDebit={handleDebit}
              />
            }
          />
          <Route path="/accounts" element={<Accounts account={account} />} />
          <Route path="/about" element={<About />} />
          <Route path="/transfer" element={<TransferFunds balance={account.balance} onTransfer={handleTransfer} />} />
          <Route path="/history" element={<Transactions history={history} />} />
          <Route path="/balance" element={<BalanceController />} />
        </Routes>
      </ThemeProvider>
    </BrowserRouter>
  );
}

export default App;

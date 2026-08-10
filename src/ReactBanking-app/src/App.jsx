import { useState } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './App.css';
import Navbar from './components/Navbar';
import Dashboard from './components/Dashboard';
import TransactionForm from './components/TransactionForm';
import TransactionHistory from './components/TransactionHistory';
import { ThemeProvider } from './context/ThemeContext';

function App() {
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
    setHistory([{ type: 'Credit', amount: value, date: new Date().toLocaleString() }, ...history]);
    setAmount('');
  };

  const handleDebit = () => {
    const value = parseFloat(amount);
    if (!value || value <= 0) return alert("Enter a valid amount");
    if (value > account.balance) return alert("Insufficient balance");
    setAccount({ ...account, balance: account.balance - value });
    setHistory([{ type: 'Debit', amount: value, date: new Date().toLocaleString() }, ...history]);
    setAmount('');
  };

  const handleTransfer = (toAccount, value) => {
    setAccount({ ...account, balance: account.balance - value });
    setHistory([
      { type: 'Transfer', amount: value, to: toAccount, date: new Date().toLocaleString() },
      ...history,
    ]);
  };

  return (
    <BrowserRouter>
      <ThemeProvider>
        <Navbar />
        <Routes>
          <Route
            path="/"
            element={
              <Dashboard
                account={account}
                amount={amount}
                setAmount={setAmount}
                handleCredit={handleCredit}
                handleDebit={handleDebit}
              />
            }
          />
          <Route path="/transfer" element={<TransactionForm balance={account.balance} onTransfer={handleTransfer} />} />
          <Route path="/history" element={<TransactionHistory history={history} />} />
        </Routes>
      </ThemeProvider>
    </BrowserRouter>
  );
}

export default App;

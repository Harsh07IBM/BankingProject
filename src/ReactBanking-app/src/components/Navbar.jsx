import { Link } from 'react-router-dom';
import './Navbar.css';

function Navbar() {
  return (
    <nav className="navbar">
      <span className="nav-brand">Web3 Ledger Bank</span>
      <div className="nav-links">
        <Link to="/">Home</Link>
        <Link to="/accounts">Accounts</Link>
        <Link to="/balance">Balance</Link>
        <Link to="/transfer">Transfer</Link>
        <Link to="/history">History</Link>
        <Link to="/about">About</Link>
      </div>
    </nav>
  );
}

export default Navbar;

import { Link } from 'react-router-dom';
import './Navbar.css';

function Navbar() {
  return (
    <nav className="navbar">
      <span className="nav-brand">Web3 Ledger Bank</span>
      <div className="nav-links">
        <Link to="/">Dashboard</Link>
        <Link to="/transfer">Transfer</Link>
        <Link to="/history">History</Link>
      </div>
    </nav>
  );
}

export default Navbar;

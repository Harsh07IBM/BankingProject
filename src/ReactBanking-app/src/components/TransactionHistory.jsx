import './TransactionHistory.css';

function TransactionHistory({ history }) {
  return (
    <div className="history-page">
      <h2>Transaction History</h2>
      {history.length === 0 ? (
        <p>No transactions yet</p>
      ) : (
        <ul>
          {history.map((txn, index) => (
            <li key={index}>
              <strong>{txn.type}</strong> — ₹{txn.amount}
              {txn.to && <> → A/C {txn.to}</>}
              <br />
              <small>{txn.date}</small>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default TransactionHistory;

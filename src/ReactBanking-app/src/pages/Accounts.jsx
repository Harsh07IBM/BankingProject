import React from 'react';
import AccountDetails from '../components/AccountDetails';

function Accounts({ account }) {
  return (
    <div className="dashboard">
      <h2>Account Overview</h2>
      <AccountDetails balance={account.balance} />
      <div style={{ marginTop: 20, textAlign: 'left', maxWidth: 400, margin: '20px auto' }}>
        <p><strong>Bank:</strong> {account.bankName}</p>
        <p><strong>Account Holder:</strong> {account.holderName}</p>
        <p><strong>Account Type:</strong> {account.accountType}</p>
        <p><strong>Current Balance:</strong> ₹{account.balance.toFixed(2)}</p>
      </div>
    </div>
  );
}

export default Accounts;

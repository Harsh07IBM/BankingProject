import React from 'react';

function About() {
  return (
    <div className="dashboard">
      <h2>About Web3 Ledger Bank</h2>
      <p style={{ maxWidth: 500, margin: '0 auto', lineHeight: 1.6 }}>
        Web3 Ledger Bank is a next-generation digital banking platform powered by
        blockchain technology. We provide secure, transparent, and decentralized
        financial services to empower individuals and businesses worldwide.
      </p>
      <h3>Our Mission</h3>
      <p style={{ maxWidth: 500, margin: '0 auto', lineHeight: 1.6 }}>
        To make financial services accessible, transparent, and trustworthy through
        cutting-edge decentralized technologies.
      </p>
      <h3>Features</h3>
      <ul style={{ listStyle: 'none', padding: 0, maxWidth: 400, margin: '0 auto', textAlign: 'left' }}>
        <li>🔒 Blockchain-Secured Transactions</li>
        <li>⚡ Instant Fund Transfers</li>
        <li>📊 Real-Time Account Monitoring</li>
        <li>🌍 Global Accessibility</li>
      </ul>
    </div>
  );
}

export default About;

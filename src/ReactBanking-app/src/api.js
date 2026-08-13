/**
 * api.js – Mock API module for Web3 Ledger Bank
 *
 * Provides helper functions that simulate network calls with Promises.
 * In a real application these would hit a REST / GraphQL backend.
 */

const SIMULATED_DELAY_MS = 300;

/** Simulates a network delay */
const delay = (ms = SIMULATED_DELAY_MS) =>
  new Promise((resolve) => setTimeout(resolve, ms));

/**
 * Fetch the current account information.
 * @returns {Promise<Object>} account details
 */
export async function getAccountInfo() {
  await delay();
  return {
    bankName: 'Web3 Ledger Bank',
    holderName: 'Harsh Kumar',
    accountType: 'Savings',
    balance: 85250.75,
  };
}

/**
 * Fetch recent transaction history.
 * @returns {Promise<Array>} list of transactions
 */
export async function getTransactions() {
  await delay();
  // Returns an empty list; real implementation would hit the server.
  return [];
}

/**
 * Post a credit or debit transaction.
 * @param {'Credit'|'Debit'} type
 * @param {number} amount
 * @returns {Promise<Object>} the recorded transaction
 */
export async function postTransaction(type, amount) {
  await delay();
  return {
    type,
    amount,
    date: new Date().toLocaleString(),
  };
}

/**
 * Post a fund transfer to another account.
 * @param {string} toAccount – 12-digit account number
 * @param {number} amount
 * @returns {Promise<Object>} the recorded transfer
 */
export async function postTransfer(toAccount, amount) {
  await delay();
  return {
    type: 'Transfer',
    amount,
    to: toAccount,
    date: new Date().toLocaleString(),
  };
}

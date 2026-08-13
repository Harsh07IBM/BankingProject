// Step 1: Import createStore from Redux
import { createStore } from 'redux';

// Initial state of the banking app
const initialState = {
  balance: 85250.75,
};

// Reducer — shows how state changes based on action type
// Handles: DEPOSIT, WITHDRAW
function balanceReducer(state = initialState, action) {
  switch (action.type) {
    case 'DEPOSIT':
      console.log(`[Redux] DEPOSIT ₹${action.payload} → New balance: ₹${state.balance + action.payload}`);
      return {
        ...state,
        balance: state.balance + action.payload,
      };

    case 'WITHDRAW':
      if (action.payload > state.balance) {
        console.warn('[Redux] WITHDRAW rejected — insufficient balance');
        return state; // no change if insufficient funds
      }
      console.log(`[Redux] WITHDRAW ₹${action.payload} → New balance: ₹${state.balance - action.payload}`);
      return {
        ...state,
        balance: state.balance - action.payload,
      };

    default:
      return state;
  }
}

// Create the Redux store with the reducer
const store = createStore(balanceReducer);

export default store;

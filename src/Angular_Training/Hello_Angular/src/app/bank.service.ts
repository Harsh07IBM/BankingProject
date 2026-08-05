import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { delay, map } from 'rxjs/operators';

export interface Transaction {
  date: string;
  description: string;
  amount: number;
  type: 'Credit' | 'Debit';
  remarks?: string;
}

@Injectable({ providedIn: 'root' })
export class BankService {
  // Sender account number is fixed for the logged-in user.
  senderAccountNumber = '123456789012';

  balance = 185430;

  // Transaction history is stored in the service so multiple components can share it.
  transactions: Transaction[] = [
    { date: '04 Aug 2025', description: 'Salary - IBM India', amount: 95000, type: 'Credit' },
    { date: '03 Aug 2025', description: 'Amazon Shopping', amount: -2499, type: 'Debit' },
    { date: '02 Aug 2025', description: 'Electricity Bill', amount: -1850, type: 'Debit' },
    { date: '01 Aug 2025', description: 'Freelance Payment', amount: 15000, type: 'Credit' },
  ];

  deposit(amount: number) {
    this.balance += amount;
  }

  withdraw(amount: number) {
    this.balance -= amount;
  }

  addTransaction(transaction: Transaction) {
    // Add the newest transaction at the top of the list.
    this.transactions.unshift(transaction);
  }

  receiverExists(accountNumber: string): Observable<boolean> {
    const validAccounts = ['987654321098', '998877665544', '112233445566'];
    return of(validAccounts.includes(accountNumber)).pipe(delay(700));
  }

  hasSufficientBalance(amount: number): Observable<boolean> {
    return of(this.balance >= amount).pipe(delay(700));
  }
}

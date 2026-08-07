import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { delay } from 'rxjs/operators';
import { AppTransaction, AuthService } from './auth.service';

export type Transaction = AppTransaction;

@Injectable({ providedIn: 'root' })
export class BankService {
  constructor(private authService: AuthService) {}

  get isLoggedIn(): boolean {
    return this.authService.isAuthenticated;
  }

  get senderAccountNumber(): string {
    return this.authService.selectedAccount ?? this.authService.session?.accountNumber ?? '';
  }

  get balance(): number {
    return this.authService.getBalance();
  }

  get transactions(): Transaction[] {
    return this.authService.getTransactions();
  }

  authenticate(username: string, password: string): boolean {
    return this.authService.authenticate(username, password);
  }

  logout() {
    this.authService.logout();
  }

  deposit(amount: number) {
    this.authService.updateBalance(amount);
  }

  withdraw(amount: number) {
    this.authService.updateBalance(-amount);
  }

  addTransaction(transaction: Transaction) {
    this.authService.addTransaction(transaction);
  }

  transfer(receiverAccountNumber: string, amount: number, remarks: string) {
    const senderAccountNumber = this.senderAccountNumber;
    if (!senderAccountNumber) {
      return;
    }

    const transferDate = new Date().toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' });

    this.authService.updateBalance(-amount);
    this.authService.addTransaction({
      date: transferDate,
      description: `Transfer to ${receiverAccountNumber}`,
      amount: -amount,
      type: 'Debit',
      remarks
    });

    this.authService.updateAccountBalance(receiverAccountNumber, amount);
    this.authService.addTransactionToAccount(receiverAccountNumber, {
      date: transferDate,
      description: `Transfer from ${senderAccountNumber}`,
      amount,
      type: 'Credit',
      remarks
    });
  }

  receiverExists(accountNumber: string): Observable<boolean> {
    const valid12Digit = /^[0-9]{12}$/.test(accountNumber);
    return of(valid12Digit).pipe(delay(700));
  }

  hasSufficientBalance(amount: number): Observable<boolean> {
    return of(this.balance >= amount).pipe(delay(700));
  }
}

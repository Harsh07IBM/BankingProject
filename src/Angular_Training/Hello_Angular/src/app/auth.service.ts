import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { inject } from '@angular/core';

export type UserRole = 'Admin' | 'Current Account Holder' | 'Savings Account Holder';
export type AccountType = 'Current' | 'Savings';

export interface AppTransaction {
  date: string;
  description: string;
  amount: number;
  type: 'Credit' | 'Debit';
  remarks?: string;
}

export interface BankAccount {
  username: string;
  password: string;
  role: UserRole;
  accountHolderName: string;
  accountType: AccountType;
  accountNumber: string;
  balance: number;
  transactions: AppTransaction[];
}

export interface AuthSession {
  username: string;
  role: UserRole;
  accountHolderName: string;
  accountType: AccountType;
  accountNumber: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly ACCOUNTS_KEY = 'bank-accounts';
  private readonly CURRENT_USER_KEY = 'bank-current-user';
  private readonly SELECTED_ACCOUNT_KEY = 'bank-selected-account';

  private accounts: BankAccount[] = [];
  private currentUser: AuthSession | null = null;
  private selectedAccountNumber: string | null = null;

  constructor() {
    this.initialize();
  }

  private initialize(): void {
    this.accounts = this.readStorage<BankAccount[]>(this.ACCOUNTS_KEY, []);
    if (!this.accounts.length) {
      this.seedAccounts();
    }

    const storedUser = this.readStorage<AuthSession | null>(this.CURRENT_USER_KEY, null);
    this.currentUser = storedUser;
    this.selectedAccountNumber = this.readStorage<string | null>(this.SELECTED_ACCOUNT_KEY, null);
  }

  private seedAccounts(): void {
    this.accounts = [
      {
        username: 'admin',
        password: 'admin123',
        role: 'Admin',
        accountHolderName: 'System Admin',
        accountType: 'Current',
        accountNumber: '000000000000',
        balance: 0,
        transactions: []
      },
      {
        username: 'currentUser',
        password: 'current123',
        role: 'Current Account Holder',
        accountHolderName: 'Asha Mehta',
        accountType: 'Current',
        accountNumber: '123456789012',
        balance: 185430,
        transactions: []
      },
      {
        username: 'savingsUser',
        password: 'savings123',
        role: 'Savings Account Holder',
        accountHolderName: 'Rohit Kumar',
        accountType: 'Savings',
        accountNumber: '987654321098',
        balance: 125000,
        transactions: []
      }
    ];
    this.saveAccounts();
  }

  private saveAccounts(): void {
    this.writeStorage(this.ACCOUNTS_KEY, this.accounts);
  }

  private readStorage<T>(key: string, fallback: T): T {
    if (typeof window === 'undefined') {
      return fallback;
    }

    const value = window.localStorage.getItem(key);
    if (!value) {
      return fallback;
    }

    try {
      return JSON.parse(value) as T;
    } catch {
      return fallback;
    }
  }

  private writeStorage<T>(key: string, value: T): void {
    if (typeof window === 'undefined') {
      return;
    }
    window.localStorage.setItem(key, JSON.stringify(value));
  }

  authenticate(username: string, password: string): boolean {
    const matchedAccount = this.accounts.find(
      (account) => account.username === username && account.password === password
    );

    if (!matchedAccount) {
      return false;
    }

    this.currentUser = {
      username: matchedAccount.username,
      role: matchedAccount.role,
      accountHolderName: matchedAccount.accountHolderName,
      accountType: matchedAccount.accountType,
      accountNumber: matchedAccount.accountNumber
    };

    this.selectedAccountNumber = matchedAccount.accountNumber;
    this.writeStorage(this.CURRENT_USER_KEY, this.currentUser);
    this.writeStorage(this.SELECTED_ACCOUNT_KEY, this.selectedAccountNumber);
    return true;
  }

  logout(): void {
    this.currentUser = null;
    this.selectedAccountNumber = null;
    if (typeof window !== 'undefined') {
      window.localStorage.removeItem(this.CURRENT_USER_KEY);
      window.localStorage.removeItem(this.SELECTED_ACCOUNT_KEY);
    }
  }

  get session(): AuthSession | null {
    return this.currentUser;
  }

  get isAuthenticated(): boolean {
    return !!this.currentUser;
  }

  get isAdmin(): boolean {
    return this.currentUser?.role === 'Admin';
  }

  get selectedAccount(): string | null {
    return this.selectedAccountNumber;
  }

  getVisibleAccounts(): BankAccount[] {
    if (!this.currentUser) {
      return [];
    }

    if (this.currentUser.role === 'Admin') {
      return this.accounts;
    }

    return this.accounts.filter((account) => account.accountNumber === this.currentUser?.accountNumber);
  }

  getActiveAccount(): BankAccount | undefined {
    const accountNumber = this.selectedAccountNumber ?? this.currentUser?.accountNumber;
    return this.accounts.find((account) => account.accountNumber === accountNumber);
  }

  setSelectedAccount(accountNumber: string | null): void {
    if (!accountNumber) {
      this.selectedAccountNumber = this.currentUser?.accountNumber ?? null;
      this.writeStorage(this.SELECTED_ACCOUNT_KEY, this.selectedAccountNumber);
      return;
    }

    const exists = this.accounts.some((account) => account.accountNumber === accountNumber);
    if (exists) {
      this.selectedAccountNumber = accountNumber;
      this.writeStorage(this.SELECTED_ACCOUNT_KEY, accountNumber);
    }
  }

  getBalance(): number {
    return this.getActiveAccount()?.balance ?? 0;
  }

  getTransactions(): AppTransaction[] {
    return this.getActiveAccount()?.transactions ?? [];
  }

  updateBalance(delta: number): void {
    const activeAccount = this.getActiveAccount();
    if (!activeAccount) {
      return;
    }

    activeAccount.balance += delta;
    this.saveAccounts();
  }

  addTransaction(transaction: AppTransaction): void {
    const activeAccount = this.getActiveAccount();
    if (!activeAccount) {
      return;
    }

    activeAccount.transactions.unshift(transaction);
    this.saveAccounts();
  }

  getAccount(accountNumber: string): BankAccount | undefined {
    return this.accounts.find((account) => account.accountNumber === accountNumber);
  }

  updateAccountBalance(accountNumber: string, delta: number): void {
    const account = this.getAccount(accountNumber);
    if (!account) {
      return;
    }

    account.balance += delta;
    this.saveAccounts();
  }

  addTransactionToAccount(accountNumber: string, transaction: AppTransaction): void {
    const account = this.getAccount(accountNumber);
    if (!account) {
      return;
    }

    account.transactions.unshift(transaction);
    this.saveAccounts();
  }
}

export const authGuard = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};

export const adminNoTransferGuard = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAdmin) {
    router.navigate(['/dashboard']);
    return false;
  }

  return true;
};

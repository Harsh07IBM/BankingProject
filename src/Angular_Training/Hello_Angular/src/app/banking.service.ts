import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Account {
  id?: number;
  name: string;
  accountNumber: string;
  balance: number;
}

@Injectable({ providedIn: 'root' })
export class BankingService {

  // Using JSONPlaceholder as a fake API for learning
  private apiUrl = 'https://jsonplaceholder.typicode.com/users';

  constructor(private http: HttpClient) {}

  // GET - Fetch all accounts
  getAccounts(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // GET - Fetch single account by ID
  getAccountById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  // POST - Create a new account
  createAccount(account: Account): Observable<any> {
    return this.http.post<any>(this.apiUrl, account);
  }

  // PATCH - Update account partially
  updateAccount(id: number, data: Partial<Account>): Observable<any> {
    return this.http.patch<any>(`${this.apiUrl}/${id}`, data);
  }

  // DELETE - Delete an account
  deleteAccount(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`);
  }
}

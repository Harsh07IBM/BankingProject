import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BankService } from './bank.service';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h3>Welcome to the Bank Dashboard</h3>
    <p>Role: <strong>{{ authService.session?.role }}</strong></p>
    <p>Account Holder: <strong>{{ currentAccount?.accountHolderName }}</strong></p>
    <p>Account Type: <strong>{{ currentAccount?.accountType }}</strong></p>
    <p>Account Number: <strong>{{ currentAccount?.accountNumber }}</strong></p>
    <p>Account Balance: <strong style="color: green;">{{ balance | currency:'INR' }}</strong></p>
    <p>Transactions Count: <strong>{{ currentAccount?.transactions?.length ?? 0 }}</strong></p>
  `,
  styles: [`
    h3 { color: darkblue; }
  `]
})
export class DashboardComponent {
  constructor(public bankService: BankService, public authService: AuthService) {}

  get balance() {
    return this.bankService.balance;
  }

  get currentAccount() {
    return this.authService.getActiveAccount();
  }
}

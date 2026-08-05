import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BankService } from './bank.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h3>Welcome to the Web3 Bank , Transactions will be stored on Blockchain</h3>
    <p>Account Holder: <strong>Harsh</strong></p>
    <p>Account Balance: <strong style="color: green;">₹ {{ balance | number:'1.2-2' }}</strong></p>
  `,
  styles: [`
    h3 { color: darkblue; }
  `]
})
export class DashboardComponent {
  constructor(private bankService: BankService) {}

  get balance() {
    return this.bankService.balance;
  }
}

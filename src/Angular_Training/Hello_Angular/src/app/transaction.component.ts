import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BankService, Transaction } from './bank.service';

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h3>Transaction History</h3>
    <table border="1">
      <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Amount</th>
        <th>Type</th>
        <th>Remarks</th>
      </tr>
      <tr *ngFor="let tx of transactions">
        <td>{{ tx.date }}</td>
        <td>{{ tx.description }}</td>
        <td [style.color]="tx.amount >= 0 ? 'green' : 'red'">
          {{ tx.amount >= 0 ? '+' : '-' }} ₹ {{ tx.amount | number:'1.0-0' }}
        </td>
        <td>{{ tx.type }}</td>
        <td>{{ tx.remarks || '-' }}</td>
      </tr>
    </table>
  `,
  styles: [
    `
    h3 { color: darkblue; }
    table { border-collapse: collapse; width: 100%; }
    th { background-color: lightblue; padding: 8px; }
    td { padding: 8px; }
  `]
})
export class TransactionComponent {
  constructor(public bankService: BankService) {}

  get transactions(): Transaction[] {
    return this.bankService.transactions;
  }
}

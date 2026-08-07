import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, Router } from '@angular/router';
import { BankService } from './bank.service';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink],
  template: `
    <h2>Harsh's Bank</h2>
    <div *ngIf="authService.isAuthenticated" style="margin-bottom: 10px;">
      <strong>Welcome, {{ authService.session?.accountHolderName }}</strong> ({{ authService.session?.role }})
      <span *ngIf="authService.isAdmin" style="margin-left: 10px;">
        <label>View account:</label>
        <select [value]="authService.selectedAccount || ''" (change)="selectAccount($event)">
          <option value="">Select account</option>
          <option *ngFor="let account of authService.getVisibleAccounts()" [value]="account.accountNumber">
            {{ account.accountHolderName }} ({{ account.accountType }}) - {{ account.accountNumber }}
          </option>
        </select>
      </span>
    </div>
    <nav *ngIf="authService.isAuthenticated">
      <a routerLink="/dashboard">Dashboard</a> |
      <a routerLink="/transactions">Transactions</a> |
      <a *ngIf="!authService.isAdmin" routerLink="/fund-transfer">Fund Transfer</a>
      <span *ngIf="!authService.isAdmin">|</span>
      <a href="#" (click)="logout($event)">Logout</a>
    </nav>
    <hr />
    <router-outlet></router-outlet>
  `,
  styles: [`
    h2 { color: darkblue; }
    nav a { color: blue; margin: 0 5px; }
    select { padding: 4px; margin-left: 6px; }
  `]
})
export class AppComponent {
  constructor(public bankService: BankService, public authService: AuthService, private router: Router) {}

  logout(event: Event) {
    event.preventDefault();
    this.bankService.logout();
    this.router.navigate(['/login']);
  }

  selectAccount(event: Event) {
    const target = event.target as HTMLSelectElement;
    this.authService.setSelectedAccount(target.value || null);
  }
}

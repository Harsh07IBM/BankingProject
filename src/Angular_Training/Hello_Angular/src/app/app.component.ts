import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  template: `
    <h2>Harsh's Bank</h2>
    <nav>
      <a routerLink="/dashboard">Dashboard</a> |
      <a routerLink="/transactions">Transactions</a> |
      <a routerLink="/fund-transfer">Fund Transfer</a>
    </nav>
    <hr />
    <router-outlet></router-outlet>
  `,
  styles: [`
    h2 { color: darkblue; }
    nav a { color: blue; margin: 0 5px; }
  `]
})
export class AppComponent { }

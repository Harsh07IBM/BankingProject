import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <h2>Harsh's Bank - Login</h2>
    <hr />
    <div>
      <label>Username: </label>
      <input type="text" [(ngModel)]="username" placeholder="Enter username" />
    </div>
    <br />
    <div>
      <label>Password: </label>
      <input type="password" [(ngModel)]="password" placeholder="Enter password" />
    </div>
    <br />
    <button (click)="login()">Login</button>

    <p *ngIf="errorMessage" style="color: red;">
      <strong>{{ errorMessage }}</strong>
    </p>
    <p style="color: gray;">Try: admin / admin123, currentUser / current123, or savingsUser / savings123</p>
  `,
  styles: [`
    h2 { color: darkblue; }
    input { padding: 5px; width: 200px; }
    button { padding: 5px 20px; cursor: pointer; }
  `]
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    if (this.authService.authenticate(this.username, this.password)) {
      this.router.navigate(['/dashboard']);
    } else {
      this.errorMessage = 'Invalid username or password!';
    }
  }
}

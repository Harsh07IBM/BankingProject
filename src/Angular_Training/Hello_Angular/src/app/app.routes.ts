import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard.component';
import { TransactionComponent } from './transaction.component';
import { FundTransferComponent } from './fundTransfer.component';
import { LoginComponent } from './login.component';
import { authGuard, adminNoTransferGuard } from './auth.service';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'transactions', component: TransactionComponent, canActivate: [authGuard] },
  { path: 'fund-transfer', component: FundTransferComponent, canActivate: [authGuard, adminNoTransferGuard] },
];

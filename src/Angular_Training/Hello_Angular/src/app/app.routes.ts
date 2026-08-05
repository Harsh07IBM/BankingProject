import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard.component';
import { TransactionComponent } from './transaction.component';
import { FundTransferComponent } from './fundTransfer.component';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'transactions', component: TransactionComponent },
  { path: 'fund-transfer', component: FundTransferComponent },
];

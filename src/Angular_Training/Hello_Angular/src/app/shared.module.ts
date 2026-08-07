import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaskAccountPipe } from './maskAccount.pipe';

@NgModule({
  imports: [CommonModule, MaskAccountPipe],
  exports: [MaskAccountPipe]
})
export class SharedModule {}

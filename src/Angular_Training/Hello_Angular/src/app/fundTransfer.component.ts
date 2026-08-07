import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators, AbstractControl, ValidationErrors, AsyncValidatorFn } from '@angular/forms';
import { BankService } from './bank.service';
import { SharedModule } from './shared.module';
import { map } from 'rxjs/operators';
import { Observable, of } from 'rxjs';

@Component({
  selector: 'app-fund-transfer',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, SharedModule],
  template: `
    <h3>Fund Transfer</h3>
    <p>Current Balance: <strong style="color: green;">{{ balance | currency:'INR' }}</strong></p>
    <form [formGroup]="transferForm" (ngSubmit)="submitTransfer()">
      <div>
        <label>Sender Account Number</label><br />
        <strong>{{ bankService.senderAccountNumber | maskAccount }}</strong>
      </div>

      <div>
        <label>Receiver Account Number</label><br />
        <input type="text" formControlName="receiverAccountNumber" placeholder="Enter receiver account" />
        <div *ngIf="receiverAccountControl.touched || receiverAccountControl.dirty">
          <small *ngIf="receiverAccountControl.hasError('required')" style="color: red;">Receiver account is required.</small>
          <small *ngIf="receiverAccountControl.hasError('pattern')" style="color: red;">Receiver account must be 12 digits.</small>
          <small *ngIf="receiverAccountControl.hasError('sameAccount')" style="color: red;">Receiver account cannot be the same as sender account.</small>
          <small *ngIf="receiverAccountControl.hasError('receiverNotFound')" style="color: red;">Receiver account does not exist.</small>
          <small *ngIf="receiverAccountControl.pending" style="color: blue;">Checking receiver account...</small>
        </div>
      </div>

      <div>
        <label>Transfer Amount</label><br />
        <input type="number" formControlName="transferAmount" placeholder="Enter amount" />
        <div *ngIf="amountControl.touched || amountControl.dirty">
          <small *ngIf="amountControl.hasError('required')" style="color: red;">Transfer amount is required.</small>
          <small *ngIf="amountControl.hasError('min')" style="color: red;">Transfer amount must be at least ₹100.</small>
          <small *ngIf="amountControl.hasError('insufficientBalance')" style="color: red;">Insufficient balance.</small>
          <small *ngIf="amountControl.pending" style="color: blue;">Checking balance...</small>
        </div>
      </div>

      <div>
        <label>Remarks</label><br />
        <textarea formControlName="remarks" placeholder="Enter remarks" rows="3"></textarea>
        <div *ngIf="remarksControl.touched || remarksControl.dirty">
          <small *ngIf="remarksControl.hasError('required')" style="color: red;">Remarks are required.</small>
          <small *ngIf="remarksControl.hasError('maxlength')" style="color: red;">Remarks cannot exceed 200 characters.</small>
        </div>
      </div>

      <div>
        <label>Saved Beneficiary</label><br />
        <select (change)="onBeneficiarySelect($any($event.target).value)">
          <option value="">Select beneficiary</option>
          <option *ngFor="let beneficiary of savedBeneficiaries.controls; let i = index" [value]="i">
            {{ beneficiary.get('name')?.value }} ({{ beneficiary.get('accountNumber')?.value }})
          </option>
        </select>
      </div>

      <div formArrayName="savedBeneficiaries">
        <h4>Saved Beneficiaries</h4>
        <div *ngFor="let beneficiary of savedBeneficiaries.controls; let i = index" [formGroupName]="i" class="beneficiary-card">
          <label>Beneficiary Name</label>
          <input type="text" formControlName="name" placeholder="Beneficiary name" />

          <label>Account Number</label>
          <input type="text" formControlName="accountNumber" placeholder="Beneficiary account" />

          <label>IFSC Code</label>
          <input type="text" formControlName="ifscCode" placeholder="IFSC code" />

          <button type="button" (click)="removeBeneficiary(i)">Remove</button>
        </div>
        <button type="button" (click)="addBeneficiary()">Add Beneficiary</button>
      </div>

      <div>
        <button type="submit" [disabled]="transferForm.invalid || transferForm.pending">Transfer</button>
      </div>
    </form>

    <p *ngIf="message" [style.color]="messageColor"><strong>{{ message }}</strong></p>
  `,
  styles: [
    `
    h3 { color: darkblue; }
    label { display: block; margin-top: 10px; font-weight: bold; }
    input, textarea, select { width: 100%; max-width: 400px; padding: 6px; margin-top: 4px; }
    button { margin-top: 10px; padding: 6px 12px; cursor: pointer; }
    .beneficiary-card { border: 1px solid #ddd; padding: 10px; margin-top: 10px; }
  `]
})
export class FundTransferComponent {
  transferForm: FormGroup;
  message = '';
  messageColor = 'green';

  constructor(public bankService: BankService, private fb: FormBuilder) {
    this.transferForm = this.fb.group({
      senderAccountNumber: [this.bankService.senderAccountNumber],
      receiverAccountNumber: ['', {
        validators: [Validators.required, Validators.pattern(/^[0-9]{12}$/), this.notSameAccountValidator()],
        asyncValidators: [this.receiverAccountValidator()],
        updateOn: 'blur'
      }],
      transferAmount: ['', {
        validators: [Validators.required, Validators.min(100)],
        asyncValidators: [this.balanceValidator()],
        updateOn: 'blur'
      }],
      remarks: ['', [Validators.required, Validators.maxLength(200)]],
      savedBeneficiaries: this.fb.array([
        this.createBeneficiary('Rahul Sharma', '987654321098', 'HDFC0001234'),
        this.createBeneficiary('Priya Singh', '998877665544', 'ICIC0005678')
      ])
    });
  }

  get balance() {
    return this.bankService.balance;
  }

  get savedBeneficiaries(): FormArray {
    return this.transferForm.get('savedBeneficiaries') as FormArray;
  }

  get receiverAccountControl() {
    return this.transferForm.get('receiverAccountNumber') as AbstractControl;
  }

  get amountControl() {
    return this.transferForm.get('transferAmount') as AbstractControl;
  }

  get remarksControl() {
    return this.transferForm.get('remarks') as AbstractControl;
  }

  createBeneficiary(name = '', accountNumber = '', ifscCode = '') {
    return this.fb.group({
      name: [name, Validators.required],
      accountNumber: [accountNumber, [Validators.required, Validators.pattern(/^[0-9]{12}$/)]],
      ifscCode: [ifscCode, Validators.required]
    });
  }

  addBeneficiary() {
    this.savedBeneficiaries.push(this.createBeneficiary());
  }

  removeBeneficiary(index: number) {
    this.savedBeneficiaries.removeAt(index);
  }

  onBeneficiarySelect(selectedIndex: string) {
    const index = Number(selectedIndex);
    if (!Number.isNaN(index) && this.savedBeneficiaries.at(index)) {
      const beneficiary = this.savedBeneficiaries.at(index).value;
      this.transferForm.patchValue({ receiverAccountNumber: beneficiary.accountNumber });
    }
  }

  notSameAccountValidator(): (control: AbstractControl) => ValidationErrors | null {
    return (control: AbstractControl): ValidationErrors | null => {
      return control.value === this.bankService.senderAccountNumber ? { sameAccount: true } : null;
    };
  }

  receiverAccountValidator(): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      const value = control.value;
      if (!value || control.hasError('pattern') || control.hasError('sameAccount')) {
        return of(null);
      }
      return this.bankService.receiverExists(value).pipe(
        map(exists => (exists ? null : { receiverNotFound: true }))
      );
    };
  }

  balanceValidator(): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      const amount = Number(control.value);
      if (!control.value || isNaN(amount) || amount < 100) {
        return of(null);
      }
      return this.bankService.hasSufficientBalance(amount).pipe(
        map(hasBalance => (hasBalance ? null : { insufficientBalance: true }))
      );
    };
  }

  submitTransfer() {
    if (this.transferForm.invalid) {
      this.transferForm.markAllAsTouched();
      return;
    }

    const formValue = this.transferForm.value;
    const amount = Number(formValue.transferAmount);
    const receiverAccountNumber = formValue.receiverAccountNumber;
    const remarks = formValue.remarks;

    this.bankService.transfer(receiverAccountNumber, amount, remarks);

    this.message = `₹ ${amount} transferred successfully to ${receiverAccountNumber}`;
    this.messageColor = 'green';

    this.transferForm.reset({ senderAccountNumber: this.bankService.senderAccountNumber });
  }
}

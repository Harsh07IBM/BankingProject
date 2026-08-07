import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'maskAccount',
  standalone: true
})
export class MaskAccountPipe implements PipeTransform {
  transform(value: string): string {
    if (!value || value.length < 4) {
      return value;
    }
    // Show only last 4 digits, mask the rest with X
    const masked = 'X'.repeat(value.length - 4) + value.slice(-4);
    return masked;
  }
}

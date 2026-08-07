import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;

  beforeEach(() => {
    window.localStorage.clear();
    TestBed.configureTestingModule({
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
  });

  afterEach(() => {
    window.localStorage.clear();
  });

  it('should authenticate valid admin credentials', () => {
    const result = service.authenticate('admin', 'admin123');

    expect(result).toBeTrue();
    expect(service.isAuthenticated).toBeTrue();
    expect(service.session?.role).toBe('Admin');
    expect(service.getVisibleAccounts().length).toBe(3);
  });

  it('should reject invalid credentials', () => {
    const result = service.authenticate('wrong', 'wrong');

    expect(result).toBeFalse();
    expect(service.isAuthenticated).toBeFalse();
    expect(service.session).toBeNull();
  });

  it('should allow admin to select a different account and view it', () => {
    service.authenticate('admin', 'admin123');
    service.setSelectedAccount('123456789012');

    expect(service.selectedAccount).toBe('123456789012');
    expect(service.getActiveAccount()?.accountHolderName).toBe('Asha Mehta');
  });
});

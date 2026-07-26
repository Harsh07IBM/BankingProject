package Junit;

enum AccountStatus {
    ACTIVE,
    BLOCKED,
    CLOSED
}

class CustomerAccount {

    private String accountNumber;
    private AccountStatus status;

    CustomerAccount(String accountNumber, AccountStatus status) {
        this.accountNumber = accountNumber;
        this.status = status;
    }

    public void displayStatus() {

        switch (status) {

            case ACTIVE:
                System.out.println(
                        accountNumber + " account is active"
                );
                break;

            case BLOCKED:
                System.out.println(
                        accountNumber + " account is blocked"
                );
                break;

            case CLOSED:
                System.out.println(
                        accountNumber + " account is closed"
                );
                break;
        }
    }
}

public class Day7_BankingEnum {

    public static void main(String[] args) {

        CustomerAccount account =
                new CustomerAccount(
                        "ACC1001",
                        AccountStatus.ACTIVE
                );

        account.displayStatus();
    }
}

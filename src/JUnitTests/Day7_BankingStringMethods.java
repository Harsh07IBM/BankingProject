package Junit;

public class Day7_BankingStringMethods {

    public static void main(String[] args) {

        String customerName = "Harsh Saini";
        String accountNumber = "ACC123456";
        String branchName = "Delhi Main Branch";
        String accountType = "Savings Account";

        System.out.println("Name Length: " + customerName.length());
        System.out.println("First Character: " + customerName.charAt(0));
        System.out.println("Uppercase Name: " + customerName.toUpperCase());
        System.out.println("Lowercase Name: " + customerName.toLowerCase());
        System.out.println("Account Type Matches: " + accountType.equals("Savings Account"));
        System.out.println("Case Ignored Check: " + accountType.equalsIgnoreCase("savings account"));
        System.out.println("Contains Savings: " + accountType.contains("Savings"));
        System.out.println("Account Starts With ACC: " + accountNumber.startsWith("ACC"));
        System.out.println("Branch Ends With Branch: " + branchName.endsWith("Branch"));
        System.out.println("Account Prefix: " + accountNumber.substring(0, 3));
        System.out.println("Position of Space: " + customerName.indexOf(" "));
        System.out.println("Updated Branch: " + branchName.replace("Delhi", "Mumbai"));

        String enteredName = "   Harsh Saini   ";
        System.out.println("Trimmed Name: " + enteredName.trim());

        String nomineeName = "";
        System.out.println("Nominee Name Empty: " + nomineeName.isEmpty());

        System.out.println("Customer Details: " + customerName.concat(" - ").concat(accountNumber));

        String fullName = "Harsh Saini";
        String[] nameParts = fullName.split(" ");

        System.out.println("First Name: " + nameParts[0]);
        System.out.println("Last Name: " + nameParts[1]);

        System.out.println("String Comparison: " + accountType.compareTo("Current Account"));

        if (accountNumber.startsWith("ACC")) {
            System.out.println("Valid Bank Account Number");
        }
    }
}

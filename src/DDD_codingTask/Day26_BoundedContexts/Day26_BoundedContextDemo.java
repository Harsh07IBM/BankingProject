// Task 17 - Demo of the two bounded contexts
//
// Both classes are named "Customer" and that is perfectly fine in DDD,
// because each one lives inside its own bounded context (its own package).
// The only thing shared between them is the customer id (the identity),
// NOT the model.
//
// How to compile and run (from this folder):
//    javac sales/Customer.java support/Customer.java Day26_BoundedContextDemo.java
//    java Day26_BoundedContextDemo

// Only one of the two can be imported by name, the other must be fully
// qualified - the compiler itself forces us to be explicit about which
// context we are talking about.
import sales.Customer;

public class Day26_BoundedContextDemo {

    public static void main(String[] args) {

        // ---------- SALES CONTEXT ----------
        Customer salesCustomer = new Customer(
                "C-1001", "Infosys Ltd", 50000, "NORTH"
        );

        System.out.println("SALES   : " + salesCustomer);
        System.out.println("Can order 40000 ? " +
                salesCustomer.canPlaceOrderOf(40000));

        salesCustomer.recordPurchase(120000);
        System.out.println("After big purchase, credit limit raised to " +
                salesCustomer.getCreditLimit());

        System.out.println();

        // ---------- SUPPORT CONTEXT ----------
        support.Customer supportCustomer = new support.Customer(
                "C-1001", "Ramesh Iyer", "PREMIUM"
        );

        System.out.println("SUPPORT : " + supportCustomer);

        supportCustomer.raiseTicket();
        supportCustomer.raiseTicket();

        System.out.println("Open tickets      : " +
                supportCustomer.getOpenTickets());
        System.out.println("Response time (h) : " +
                supportCustomer.responseTimeInHours());

        supportCustomer.closeTicket(90);
        System.out.println("After closing one : " + supportCustomer);

        System.out.println();

        // ---------- WHAT THE BOUNDARY GIVES US ----------
        System.out.println("Same identity in both contexts ? " +
                salesCustomer.getCustomerId()
                        .equals(supportCustomer.getCustomerId()));

        // These lines do NOT compile - the boundary is enforced by Java:
        //
        // supportCustomer.getCreditLimit();
        //     -> credit limit does not exist in the Support model
        //
        // salesCustomer.raiseTicket();
        //     -> raising tickets does not exist in the Sales model
        //
        // salesCustomer.applyInternalSalesDiscountPolicy();
        //     -> package-private, only visible inside the sales package
        //
        // Customer c = supportCustomer;
        //     -> the two Customer types are NOT assignable to each other
        System.out.println("Two Customer classes, two models, one id.");
    }
}

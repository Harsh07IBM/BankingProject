// Task 17 (Coding 1 + 2) - SALES bounded context
// In the Sales context a "Customer" means a buyer.
// The words used here (credit limit, discount, lifetime value) belong to the
// ubiquitous language of the Sales team only.
//
// The package "sales" is the boundary. Everything that is internal to Sales is
// package-private, so the Support context physically cannot touch it.

package sales;

public class Customer {

    private final String customerId;
    private String companyName;
    private double creditLimit;      // meaningful only for Sales
    private double lifetimeValue;    // meaningful only for Sales
    private String salesRegion;

    public Customer(String customerId,
                    String companyName,
                    double creditLimit,
                    String salesRegion) {

        this.customerId = customerId;
        this.companyName = companyName;
        this.creditLimit = creditLimit;
        this.salesRegion = salesRegion;
        this.lifetimeValue = 0;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public String getSalesRegion() {
        return salesRegion;
    }

    public double getLifetimeValue() {
        return lifetimeValue;
    }

    // Sales specific business rule
    public boolean canPlaceOrderOf(double orderAmount) {
        return orderAmount <= creditLimit;
    }

    public void recordPurchase(double amount) {

        this.lifetimeValue += amount;

        // rewarding loyal buyers is a Sales concern
        if (this.lifetimeValue > 100000) {
            this.creditLimit = this.creditLimit * 1.10;
        }
    }

    // package-private: only Sales classes may use this, Support cannot
    void applyInternalSalesDiscountPolicy() {
        // internal to the Sales context
    }

    @Override
    public String toString() {
        return "sales.Customer{id=" + customerId +
                ", company=" + companyName +
                ", creditLimit=" + creditLimit +
                ", region=" + salesRegion + "}";
    }
}

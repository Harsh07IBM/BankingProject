// Task 16 (Coding 1) - DDD Entity
// A Customer is an ENTITY: it has an identity (id) that never changes.
// Two customers are the same customer if their id is the same,
// even if the name or any other attribute changes later.

class Customer {

    private final String id;
    private String name;

    public Customer(String id, String name) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "Customer id is required"
            );
        }

        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Attributes can change during the lifecycle of the entity
    public void rename(String newName) {

        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException(
                    "Customer name cannot be empty"
            );
        }

        this.name = newName;
    }

    // Identity based equality - only the id is compared
    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }

        if (!(other instanceof Customer)) {
            return false;
        }

        Customer that = (Customer) other;

        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Customer{id=" + id + ", name=" + name + "}";
    }
}

public class Day26_CustomerEntity {

    public static void main(String[] args) {

        Customer customer = new Customer("C-1001", "Harsh Saini");

        System.out.println("Before change : " + customer);

        // Attribute changes, identity does not
        customer.rename("Harsh Kumar Saini");

        System.out.println("After change  : " + customer);

        // Same id, different name -> still the SAME customer
        Customer sameCustomerFromDb =
                new Customer("C-1001", "Harsh Kumar Saini");

        Customer differentCustomer =
                new Customer("C-2002", "Harsh Saini");

        System.out.println("Same id equal?      : " +
                customer.equals(sameCustomerFromDb));

        System.out.println("Different id equal? : " +
                customer.equals(differentCustomer));
    }
}

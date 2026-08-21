// Task 18 (Coding 1) - Aggregate Root
//
// Order          -> aggregate root (entity, has identity, guards invariants)
// OrderLine      -> entity INSIDE the aggregate (identity is local to the order)
// Money          -> value object (reused from Day26_Money.java in this folder)
//
// Rules of the aggregate:
//   1. Nothing outside the aggregate may hold a reference to an OrderLine.
//   2. The total is recalculated by the root every time lines change.
//   3. All invariants (single currency, no empty quantity, no change after
//      confirmation) are enforced by the root, never by the caller.

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Entity inside the aggregate - constructor is package-private so only the
// aggregate root in the same package can create one.
class OrderLine {

    private final String lineId;
    private final String productName;
    private final Money unitPrice;
    private int quantity;

    OrderLine(String lineId, String productName, Money unitPrice, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero"
            );
        }

        this.lineId = lineId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getLineId() {
        return lineId;
    }

    public String getProductName() {
        return productName;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    void increaseQuantity(int extra) {

        if (extra <= 0) {
            throw new IllegalArgumentException("Extra quantity must be positive");
        }

        this.quantity += extra;
    }

    // line total is derived, never stored
    public Money lineTotal() {
        return unitPrice.multiply(quantity);
    }

    // identity inside the aggregate
    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }

        if (!(other instanceof OrderLine)) {
            return false;
        }

        return this.lineId.equals(((OrderLine) other).lineId);
    }

    @Override
    public int hashCode() {
        return lineId.hashCode();
    }

    @Override
    public String toString() {
        return productName + " x" + quantity +
                " @ " + unitPrice + " = " + lineTotal();
    }
}

// AGGREGATE ROOT - the only entry point into this object graph
class Order {

    private final String orderId;
    private final String currency;
    private final List<OrderLine> lines = new ArrayList<>();
    private Money total;
    private boolean confirmed;
    private int lineCounter;

    public Order(String orderId, String currency) {

        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order id is required");
        }

        this.orderId = orderId;
        this.currency = currency.toUpperCase();
        this.total = Money.zero(this.currency);
        this.confirmed = false;
    }

    public String getOrderId() {
        return orderId;
    }

    public Money getTotal() {
        return total;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    // outside code can look but cannot modify the internals
    public List<OrderLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    // ---------- behaviour that changes the aggregate ----------

    public void addLine(String productName, Money unitPrice, int quantity) {

        requireNotConfirmed();

        if (!unitPrice.getCurrency().equals(this.currency)) {
            throw new IllegalArgumentException(
                    "Order currency is " + currency +
                            " but line price is " + unitPrice.getCurrency()
            );
        }

        // invariant: same product is merged instead of duplicated
        for (OrderLine existing : lines) {
            if (existing.getProductName().equalsIgnoreCase(productName)
                    && existing.getUnitPrice().equals(unitPrice)) {

                existing.increaseQuantity(quantity);
                recalculateTotal();
                return;
            }
        }

        lineCounter++;

        lines.add(new OrderLine(
                orderId + "-L" + lineCounter, productName, unitPrice, quantity
        ));

        recalculateTotal();
    }

    public void removeLine(String lineId) {

        requireNotConfirmed();

        boolean removed = lines.removeIf(l -> l.getLineId().equals(lineId));

        if (!removed) {
            throw new IllegalArgumentException("No such line: " + lineId);
        }

        recalculateTotal();
    }

    public void confirm() {

        // invariant: an order cannot be confirmed with nothing in it
        if (lines.isEmpty()) {
            throw new IllegalStateException(
                    "Cannot confirm an order with no lines"
            );
        }

        this.confirmed = true;
    }

    private void requireNotConfirmed() {

        if (confirmed) {
            throw new IllegalStateException(
                    "Order " + orderId + " is confirmed and cannot be changed"
            );
        }
    }

    // the root owns the total - it is always consistent with the lines
    private void recalculateTotal() {

        Money sum = Money.zero(currency);

        for (OrderLine line : lines) {
            sum = sum.add(line.lineTotal());
        }

        this.total = sum;
    }

    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }

        if (!(other instanceof Order)) {
            return false;
        }

        return this.orderId.equals(((Order) other).orderId);
    }

    @Override
    public int hashCode() {
        return orderId.hashCode();
    }
}

public class Day26_OrderAggregate {

    public static void main(String[] args) {

        Order order = new Order("ORD-9001", "INR");

        System.out.println("Empty order total : " + order.getTotal());

        order.addLine("Laptop", Money.of(55000.00, "INR"), 1);
        System.out.println("After laptop      : " + order.getTotal());

        order.addLine("Mouse", Money.of(750.50, "INR"), 2);
        System.out.println("After 2 mouse     : " + order.getTotal());

        // same product + same price -> merged into the existing line
        order.addLine("Mouse", Money.of(750.50, "INR"), 1);
        System.out.println("After 1 more mouse: " + order.getTotal());

        System.out.println("\nLines:");
        order.getLines().forEach(l -> System.out.println("  " + l));

        // removing a line also recalculates the total
        String laptopLineId = order.getLines().get(0).getLineId();
        order.removeLine(laptopLineId);
        System.out.println("\nAfter removing laptop : " + order.getTotal());

        // ---------- invariants enforced by the root ----------

        try {
            order.getLines().add(null);   // list is unmodifiable
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify lines from outside the aggregate");
        }

        try {
            order.addLine("Keyboard", Money.of(20.00, "USD"), 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Blocked : " + e.getMessage());
        }

        try {
            order.addLine("Keyboard", Money.of(1200.00, "INR"), 0);
        } catch (IllegalArgumentException e) {
            System.out.println("Blocked : " + e.getMessage());
        }

        order.confirm();
        System.out.println("Order confirmed, total = " + order.getTotal());

        try {
            order.addLine("Monitor", Money.of(9000.00, "INR"), 1);
        } catch (IllegalStateException e) {
            System.out.println("Blocked : " + e.getMessage());
        }
    }
}

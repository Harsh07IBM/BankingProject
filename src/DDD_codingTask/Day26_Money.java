// Task 16 (Coding 2) - DDD Value Object
// Money is a VALUE OBJECT:
//   1. It has no id.
//   2. It is immutable - every operation returns a NEW Money.
//   3. Equality is based on the VALUE (amount + currency), not on identity.

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

final class Money {

    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) {

        if (amount == null) {
            throw new IllegalArgumentException("Amount is required");
        }

        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency is required");
        }

        // normalised so 100 and 100.00 are treated as the same value
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency.toUpperCase();
    }

    // convenience factory
    public static Money of(double amount, String currency) {
        return new Money(BigDecimal.valueOf(amount), currency);
    }

    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    // Immutable behaviour - the original object is never modified
    public Money add(Money other) {

        requireSameCurrency(other);

        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {

        requireSameCurrency(other);

        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    public Money multiply(int quantity) {

        return new Money(
                this.amount.multiply(BigDecimal.valueOf(quantity)),
                this.currency
        );
    }

    private void requireSameCurrency(Money other) {

        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "Currency mismatch: " + this.currency +
                            " and " + other.currency
            );
        }
    }

    // Value based equality
    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }

        if (!(other instanceof Money)) {
            return false;
        }

        Money that = (Money) other;

        return this.amount.compareTo(that.amount) == 0
                && this.currency.equals(that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros(), currency);
    }

    @Override
    public String toString() {
        return currency + " " + amount;
    }
}

public class Day26_Money {

    public static void main(String[] args) {

        Money price = Money.of(500.00, "INR");
        Money tax = Money.of(90.00, "INR");

        Money total = price.add(tax);

        System.out.println("Price (unchanged) : " + price);
        System.out.println("Tax               : " + tax);
        System.out.println("Total (new object): " + total);

        // Two separately created objects with the same value are equal
        Money a = Money.of(590.00, "INR");
        Money b = new Money(new java.math.BigDecimal("590"), "inr");

        System.out.println("a equals b ?      : " + a.equals(b));
        System.out.println("same reference ?  : " + (a == b));
        System.out.println("total equals a ?  : " + total.equals(a));

        // Different currency -> not equal, and cannot be added
        Money usd = Money.of(590.00, "USD");
        System.out.println("INR equals USD ?  : " + a.equals(usd));

        try {
            a.add(usd);
        } catch (IllegalArgumentException e) {
            System.out.println("Add failed        : " + e.getMessage());
        }
    }
}

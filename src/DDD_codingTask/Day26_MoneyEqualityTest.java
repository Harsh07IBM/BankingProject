// Task 18 (Coding 2) - Value object equality test
//
// Two Money objects created completely separately must be EQUAL when the
// amount and the currency match, because a value object has no identity.
// They must NOT be equal when the currency differs.
//
// Uses JUnit 5 (same setup as the Day 7 JUnit tasks).
// Money comes from Day26_Money.java in this same folder.

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Day26_MoneyEqualityTest {

    @Test
    @DisplayName("Two separately created Money objects with same value are equal")
    void sameAmountAndCurrencyAreEqual() {

        Money first = Money.of(250.00, "INR");
        Money second = Money.of(250.00, "INR");

        // different objects in memory
        assertNotSame(first, second);

        // but the same value
        assertEquals(first, second);

        // equal objects must have equal hash codes (HashMap / HashSet rely on it)
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    @DisplayName("Scale does not matter: 250 equals 250.00")
    void differentScaleSameValueIsEqual() {

        Money withScale = new BigDecimalMoneyHelper("250.00").money();
        Money withoutScale = new BigDecimalMoneyHelper("250").money();

        assertEquals(withScale, withoutScale);
    }

    @Test
    @DisplayName("Same amount but different currency is NOT equal")
    void differentCurrencyIsNotEqual() {

        Money inr = Money.of(250.00, "INR");
        Money usd = Money.of(250.00, "USD");

        assertNotEquals(inr, usd);
    }

    @Test
    @DisplayName("Different amount in same currency is NOT equal")
    void differentAmountIsNotEqual() {

        assertNotEquals(Money.of(250.00, "INR"), Money.of(251.00, "INR"));
    }

    @Test
    @DisplayName("Money is immutable - add() returns a new object")
    void addDoesNotMutateOriginal() {

        Money original = Money.of(100.00, "INR");
        Money result = original.add(Money.of(50.00, "INR"));

        // original untouched
        assertEquals(Money.of(100.00, "INR"), original);

        // new object with the new value
        assertEquals(Money.of(150.00, "INR"), result);
        assertNotSame(original, result);
    }

    @Test
    @DisplayName("Adding two different currencies is rejected")
    void cannotAddDifferentCurrencies() {

        Money inr = Money.of(100.00, "INR");
        Money usd = Money.of(100.00, "USD");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> inr.add(usd)
        );

        assertTrue(ex.getMessage().contains("Currency mismatch"));
    }

    // small helper so the scale test can build Money from an exact string
    private static class BigDecimalMoneyHelper {

        private final String raw;

        BigDecimalMoneyHelper(String raw) {
            this.raw = raw;
        }

        Money money() {
            return new Money(new BigDecimal(raw), "INR");
        }
    }
}

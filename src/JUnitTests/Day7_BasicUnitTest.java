package Junit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day7_BasicUnitTest {

    @Test
    void testAdditionPass() {

        Day7_Calculator calculator =
                new Day7_Calculator();

        assertEquals(
                15,
                calculator.add(10, 5)
        );
    }

    @Test
    void testAdditionFail() {

        Day7_Calculator calculator =
                new Day7_Calculator();

        assertEquals(
                20,
                calculator.add(10, 5),
                "Expected 20 but actual result is 15"
        );
    }
}

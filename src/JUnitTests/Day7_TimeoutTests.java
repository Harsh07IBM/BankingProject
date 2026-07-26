package Junit;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeout;

class Day7_TimeoutTests {

    @Test
    void testFastOperationPasses() {

        Day7_PerformanceService service =
                new Day7_PerformanceService();

        assertTimeout(
                Duration.ofMillis(500),
                () -> service.quickOperation()
        );
    }

    @Test
    void testSlowOperationFails() {

        Day7_PerformanceService service =
                new Day7_PerformanceService();

        assertTimeout(
                Duration.ofMillis(500),
                () -> service.slowOperation()
        );
    }

    @Test
    void testVerySmallTimeoutFails() {

        Day7_PerformanceService service =
                new Day7_PerformanceService();

        assertTimeout(
                Duration.ofMillis(50),
                () -> service.quickOperation()
        );
    }
}

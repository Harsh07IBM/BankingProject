package Junit;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class Day7_LifeCycleTest {

    private Day7_DatabaseService database;

    @BeforeEach
    void setUp() {

        database = new Day7_DatabaseService();

        database.connect();
    }

    @AfterEach
    void tearDown() {

        database.disconnect();
    }

    @Test
    void testSaveAndFindPass() {

        database.save("customer1", "Harsh");

        assertEquals("Harsh", database.find("customer1"));
    }

    @Test
    void testSaveAndFindFail() {

        database.save("customer1", "Harsh");

        assertEquals("WrongName", database.find("customer1"));
    }

    @Test
    void testMissingRecordFail() {

        assertEquals("SomeValue", database.find("unknown"));
    }
}

package Junit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class Day7_ParameterizedTests {

    @ParameterizedTest
    @ValueSource(strings = {"madam", "level", "radar"})
    void testPalindromePass(String word) {

        assertTrue(
                Day7_StringUtility.isPalindrome(word)
        );
    }

    @Test
    void testPalindromeFail() {

        assertTrue(
                Day7_StringUtility.isPalindrome("hello"),
                "Expected palindrome but value is not a palindrome"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"java", "spring", "bank"})
    void testNotPalindrome(String word) {

        assertFalse(
                Day7_StringUtility.isPalindrome(word)
        );
    }
}

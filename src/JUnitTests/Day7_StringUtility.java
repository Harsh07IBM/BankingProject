package Junit;

public class Day7_StringUtility {

    public static boolean isPalindrome(String text) {

        if (text == null) {
            return false;
        }

        String reversed =
                new StringBuilder(text)
                        .reverse()
                        .toString();

        return text.equalsIgnoreCase(reversed);
    }
}

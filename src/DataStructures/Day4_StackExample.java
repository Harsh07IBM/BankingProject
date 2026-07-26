import java.util.Stack;

public class Day4_StackExample {

    public static void main(String[] args) {

        Stack<String> books = new Stack<>();

        books.push("Java");
        books.push("C++");
        books.push("Python");

        System.out.println("Top Book: " + books.peek());

        System.out.println("Removed Book: " + books.pop());

        System.out.println("Stack Empty: " + books.empty());
    }
}

import java.util.ArrayDeque;
import java.util.Deque;

public class Day4_ArrayDequeStack {

    public static void main(String[] args) {

        Deque<String> items = new ArrayDeque<>();

        items.push("One");
        items.push("Two");
        items.push("Three");
        items.push("Four");

        items.pop();

        System.out.println("Stack Elements: " + items);
    }
}

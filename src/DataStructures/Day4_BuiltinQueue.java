import java.util.LinkedList;
import java.util.Queue;

public class Day4_BuiltinQueue {

    public static void main(String[] args) {

        Queue<String> messages = new LinkedList<>();

        messages.add("Hello");
        messages.add("Good Morning");
        messages.add("How are you?");

        System.out.println("Queue Elements: " + messages);
    }
}

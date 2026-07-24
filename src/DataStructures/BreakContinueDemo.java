public class BreakContinueDemo {
    public static void main(String[] args) {
        System.out.println("Using Break");

        for (int i = 1; i <= 5; i++) {
            if (i == 3)
                break;

            System.out.println(i);
        }

        System.out.println("Using Continue");

        for (int i = 1; i <= 5; i++) {
            if (i == 3)
                continue;

            System.out.println(i);
        }
    }
}

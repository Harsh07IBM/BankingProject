import java.util.Scanner;

public class CustomerNames {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] names = new String[3];

        for (int i = 0; i < names.length; i++) {
            names[i] = sc.nextLine();
        }

        for (String name : names) {
            System.out.println(name);
        }
    }
}

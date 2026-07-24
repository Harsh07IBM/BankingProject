import java.util.Scanner;

public class LoginWithWhile {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Enter Login Id: ");
            String id = sc.nextLine();

            System.out.print("Enter Password: ");
            String pwd = sc.nextLine();

            if (id.equals("Prasunamba") && pwd.equals("4321")) {
                System.out.println("Welcome");
                break;
            } else {
                System.out.println("Invalid Login");
            }
        }
    }
}

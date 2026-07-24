import java.util.Scanner;

public class LoginWithDoWhile {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String id;
        String pwd;

        do {
            System.out.print("Enter Login Id: ");
            id = sc.nextLine();

            System.out.print("Enter Password: ");
            pwd = sc.nextLine();
        } while (!(id.equals("Prasunamba") && pwd.equals("4321")));

        System.out.println("Welcome");
    }
}

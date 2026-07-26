public class Day4_TypeCasting {

    public static void main(String[] args) {

        int marks = 85;

        double result = marks;

        System.out.println("Integer Value: " + marks);
        System.out.println("Double Value: " + result);

        double price = 99.99;

        int newPrice = (int) price;

        System.out.println("Original Price: " + price);
        System.out.println("After Casting: " + newPrice);
    }
}

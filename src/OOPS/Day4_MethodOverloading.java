class MathOperation {

    int multiply(int a, int b) {
        return a * b;
    }

    int multiply(int a, int b, int c) {
        return a * b * c;
    }

    double multiply(double a, double b) {
        return a * b;
    }
}

public class Day4_MethodOverloading {

    public static void main(String[] args) {

        MathOperation operation = new MathOperation();

        System.out.println(operation.multiply(5, 4));
        System.out.println(operation.multiply(2, 3, 4));
        System.out.println(operation.multiply(2.5, 4.0));
    }
}
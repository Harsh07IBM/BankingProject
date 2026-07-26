class Vehicle {

    void start() {
        System.out.println("Vehicle is starting");
    }
}

class Car extends Vehicle {

    @Override
    void start() {
        System.out.println("Car starts with a key");
    }
}

public class Day4_MethodOverriding {

    public static void main(String[] args) {

        Car myCar = new Car();

        myCar.start();
    }
}

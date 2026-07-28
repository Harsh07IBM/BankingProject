# Order Service Demo

This project demonstrates a clean implementation of an `OrderService` that utilizes Dependency Injection to interact with a `PaymentService`. The project is structured to allow for easy extension and testing of payment processing methods.

## Project Structure

```
order-service-demo
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── orderservice
│   │   │               ├── Application.java
│   │   │               ├── config
│   │   │               │   └── DependencyInjector.java
│   │   │               ├── model
│   │   │               │   └── Order.java
│   │   │               └── service
│   │   │                   ├── PaymentService.java
│   │   │                   ├── CreditCardPayment.java
│   │   │                   ├── DebitCardPayment.java
│   │   │                   ├── OrderService.java
│   │   │                   └── OrderServiceImpl.java
│   │   └── resources
│   │       └── application.properties
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── orderservice
│                       └── OrderServiceImplTest.java
├── pom.xml
└── README.md
```

## Setup Instructions

1. **Clone the repository**:
   ```
   git clone <repository-url>
   cd order-service-demo
   ```

2. **Build the project**:
   Ensure you have Maven installed, then run:
   ```
   mvn clean install
   ```

3. **Run the application**:
   You can run the application using:
   ```
   mvn exec:java -Dexec.mainClass="com.example.orderservice.Application"
   ```

## Usage

The `OrderService` can be used to process orders with different payment methods. The application demonstrates manual dependency injection, allowing you to switch between `CreditCardPayment` and `DebitCardPayment` implementations without modifying the `OrderService` class.

## Testing

Unit tests for the `OrderServiceImpl` are located in the `src/test/java/com/example/orderservice/OrderServiceImplTest.java` file. You can run the tests using:
```
mvn test
```

## Dependencies

This project uses Maven for dependency management. The `pom.xml` file contains all necessary dependencies for building and running the application.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
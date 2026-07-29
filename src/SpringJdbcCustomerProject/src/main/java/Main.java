import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args)
            throws SQLException, ClassNotFoundException {

        System.out.println(
                "Starting Spring Application..."
        );

        // Start Spring Container
        ApplicationContext context =
                new ClassPathXmlApplicationContext(
                        "beans.xml"
                );

        // Get CustomerDAO object from Spring
        CustomerDAO customerDAO =
                context.getBean(
                        "customerDAO",
                        CustomerDAO.class
                );

        // Create Database
        customerDAO.createDatabase();

        // Create Table
        customerDAO.createTable();

        // Insert Records
        customerDAO.insertRecords();

        // Fetch and Display Records
        customerDAO.selectAllRows();
    }
}
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerDAO {

    private String driver;
    private String url;
    private String userName;
    private String password;


    // Setter methods
    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // Create Database
    public void createDatabase()
            throws ClassNotFoundException, SQLException {

        System.out.println("Creating database...");

        Class.forName(driver);

        String serverUrl =
                "jdbc:mysql://localhost:3306";

        Connection con =
                DriverManager.getConnection(
                        serverUrl,
                        userName,
                        password
                );

        Statement stmt =
                con.createStatement();

        String query =
                "CREATE DATABASE IF NOT EXISTS customerdb";

        stmt.executeUpdate(query);

        System.out.println(
                "Database customerdb created successfully."
        );

        con.close();
    }


    // Create Table
    public void createTable()
            throws ClassNotFoundException, SQLException {

        System.out.println("Creating table...");

        Class.forName(driver);

        Connection con =
                DriverManager.getConnection(
                        url,
                        userName,
                        password
                );

        Statement stmt =
                con.createStatement();

        String query =
                "CREATE TABLE IF NOT EXISTS CustomerInfo (" +
                "customerId INT PRIMARY KEY, " +
                "customerName VARCHAR(100), " +
                "customerFees DOUBLE, " +
                "custAddress VARCHAR(200)" +
                ")";

        stmt.executeUpdate(query);

        System.out.println(
                "CustomerInfo table created successfully."
        );

        con.close();
    }


    // Insert Records
    public void insertRecords()
            throws ClassNotFoundException, SQLException {

        System.out.println("Inserting records...");

        Class.forName(driver);

        Connection con =
                DriverManager.getConnection(
                        url,
                        userName,
                        password
                );

        Statement stmt =
                con.createStatement();

        String query =
                "INSERT IGNORE INTO CustomerInfo " +
                "(customerId, customerName, customerFees, custAddress) " +
                "VALUES " +
                "(1, 'Harsh', 5000, 'Delhi'), " +
                "(2, 'Rahul', 6000, 'Mumbai'), " +
                "(3, 'Aman', 4500, 'Bangalore')";

        stmt.executeUpdate(query);

        System.out.println(
                "Customer records inserted successfully."
        );

        con.close();
    }


    // Fetch Records
    public void selectAllRows()
            throws ClassNotFoundException, SQLException {

        System.out.println(
                "Retrieving customer data..."
        );

        Class.forName(driver);

        Connection con =
                DriverManager.getConnection(
                        url,
                        userName,
                        password
                );

        Statement stmt =
                con.createStatement();

        ResultSet rs =
                stmt.executeQuery(
                        "SELECT * FROM CustomerInfo"
                );

        System.out.println(
                "\nCustomer Records:"
        );

        while (rs.next()) {

            int customerId =
                    rs.getInt("customerId");

            String customerName =
                    rs.getString("customerName");

            double customerFees =
                    rs.getDouble("customerFees");

            String custAddress =
                    rs.getString("custAddress");

            System.out.println(
                    customerId + " " +
                    customerName + " " +
                    customerFees + " " +
                    custAddress
            );
        }

        con.close();
    }
}
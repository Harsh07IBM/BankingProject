package org.example;

import java.sql.*;

public class Main {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/bankdb";
        String username = "postgres";
        String password = "Postgre@01";

        try {

            Connection connection =
                    DriverManager.getConnection(url, username, password);

            System.out.println("Connected Successfully");

            Statement statement =
                    connection.createStatement();

            ResultSet result =
                    statement.executeQuery("SELECT * FROM Accounts");

            while(result.next()){

                System.out.println(
                        result.getInt("id") + " " +
                                result.getString("name") + " " +
                                result.getString("email") + " " +
                                result.getDouble("balance")
                );

            }

            connection.close();

        }catch(Exception e){

            e.printStackTrace();

        }

    }

}
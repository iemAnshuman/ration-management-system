package ration;

import java.sql.Connection;
import java.sql.DriverManager;

public class Test {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ration_db",
                    "root",
                    "root"
            );

            System.out.println("Connection Successful!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

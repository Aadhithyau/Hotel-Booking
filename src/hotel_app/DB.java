package hotel_app;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_app";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            con.setAutoCommit(true);  // ensure all changes are committed automatically
            return con;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

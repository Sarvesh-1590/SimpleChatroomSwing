import java.sql.*;

public class DBConnection {
    static final String URL = "jdbc:mysql://localhost:3306/chatdb";
    static final String USER = "root";          // your MySQL username
    static final String PASS = "kira"; // your MySQL password

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

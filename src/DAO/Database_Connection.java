package DAO;

import java.sql.*;

// Nguyễn Văn Tiến
public class Database_Connection {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/qlbanhang";
    private static final String user = "root";
    private static final String password = "123456";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}

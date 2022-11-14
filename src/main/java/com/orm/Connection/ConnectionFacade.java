package com.orm.Connection;
import java.sql.*;

public class ConnectionFacade {

    private static final String DATA_BASE = "user_data";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "";

    public static java.sql.Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + DATA_BASE, USER_NAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

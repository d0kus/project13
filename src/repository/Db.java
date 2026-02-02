package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Db {
    private static final String URL  = "jdbc:postgresql://localhost:5432/simpledb";
    private static final String USER = "postgres";
    private static final String PASS = "0000";

    private Db() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
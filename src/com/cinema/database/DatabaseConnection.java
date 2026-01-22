package com.cinema.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        Properties props = new Properties();
        try {
            InputStream input = DatabaseConnection.class.getClassLoader()
                    .getResourceAsStream("database.properties");

            if (input == null) {
                URL = "jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:6543/postgres?sslmode=require";
                USERNAME = "postgres.kyagpwhizcrhlhypmtyi";
                PASSWORD = "oopgrind2026";
                return;
            }

            props.load(input);
            URL = props.getProperty("db.url") + "?sslmode=require";
            USERNAME = props.getProperty("db.username");
            PASSWORD = props.getProperty("db.password");
            input.close();

        } catch (Exception e) {
            URL = "jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:6543/postgres?sslmode=require";
            USERNAME = "postgres.kyagpwhizcrhlhypmtyi";
            PASSWORD = "oopgrind2026";
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");

            Properties props = new Properties();
            props.setProperty("user", USERNAME);
            props.setProperty("password", PASSWORD);
            props.setProperty("ssl", "true");
            props.setProperty("sslmode", "require");

            return DriverManager.getConnection(URL, props);

        } catch (ClassNotFoundException e) {
            throw new SQLException("драйвер", e);
        }
    }

    public static void testConnection() {
        try {
            Connection conn = getConnection();
            System.out.println("Database connection successful!");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
}
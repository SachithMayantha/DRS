package com.example.drsystemserver.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author jayath
 * class for local mysql database connection
 */
public class DatabaseConnection {

    private static final String url = "jdbc:mysql://localhost:3306/";
    private static final String dbName = "drs";
    // change user, according to your local mysql instance
    private static final String user = "root";
    // change password, according to your local mysql instance
    private static final String password = "root";

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            Statement stmt = connection.createStatement();

            // Create database if it doesn't exist
            String createDbQuery = "CREATE DATABASE IF NOT EXISTS " + dbName;
            stmt.executeUpdate(createDbQuery);
            connection.setCatalog(dbName);
            
            // Create tables if they do not exist
            createTables(connection);
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
        return connection;
    }

    private static void createTables(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();

        // SQL to create department table
        String departmentTable = "CREATE TABLE IF NOT EXISTS department (" +
                "departmentId INT(11) NOT NULL AUTO_INCREMENT, " +
                "name VARCHAR(45) NOT NULL, " +
                "email VARCHAR(45) NOT NULL, " +
                "mobile VARCHAR(45) NOT NULL, " +
                "userId INT(11) NOT NULL, " +
                "departmentType VARCHAR(10) NOT NULL, " +
                "PRIMARY KEY (departmentId)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;";

        // SQL to create disaster table
        String disasterTable = "CREATE TABLE IF NOT EXISTS disaster (" +
                "disasterId INT(11) NOT NULL AUTO_INCREMENT, " +
                "type VARCHAR(50) NOT NULL, " +
                "location VARCHAR(100) NOT NULL, " +
                "description TEXT NOT NULL, " +
                "severity VARCHAR(50) NOT NULL, " +
                "date DATE NOT NULL, " +
                "reportedBy INT(100) NOT NULL, " +
                "locationType VARCHAR(50), " +
                "priorityNo INT(11), " +
                "image MEDIUMBLOB, " +
                "PRIMARY KEY (disasterId)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;";

        // SQL to create fire table
        String fireTable = "CREATE TABLE IF NOT EXISTS fire (" +
                "fireId INT(11) NOT NULL AUTO_INCREMENT, " +
                "disasterId INT(11) NOT NULL, " +
                "fighters INT(11), " +
                "supporters INT(11), " +
                "suppression INT(11), " +
                "status VARCHAR(45) NOT NULL DEFAULT 'Pending', " +
                "PRIMARY KEY (fireId), " +
                "FOREIGN KEY (disasterId) REFERENCES disaster(disasterId)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;";

        // SQL to create health table
        String healthTable = "CREATE TABLE IF NOT EXISTS health (" +
                "healthId INT(11) NOT NULL AUTO_INCREMENT, " +
                "disasterId INT(11) NOT NULL, " +
                "doctors INT(11), " +
                "nurses INT(11), " +
                "ambulances INT(11), " +
                "status VARCHAR(45) NOT NULL DEFAULT 'Pending', " +
                "PRIMARY KEY (healthId), " +
                "FOREIGN KEY (disasterId) REFERENCES disaster(disasterId)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;";

        // SQL to create police table
        String policeTable = "CREATE TABLE IF NOT EXISTS police (" +
                "policeId INT(11) NOT NULL AUTO_INCREMENT, " +
                "disasterId INT(11) NOT NULL, " +
                "policeman INT(11), " +
                "trafficControllers INT(11), " +
                "investigators INT(11), " +
                "status VARCHAR(45) NOT NULL DEFAULT 'Pending', " +
                "PRIMARY KEY (policeId), " +
                "FOREIGN KEY (disasterId) REFERENCES disaster(disasterId)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;";

        // SQL to create user table
        String userTable = "CREATE TABLE IF NOT EXISTS user (" +
                "userId INT(11) NOT NULL AUTO_INCREMENT, " +
                "name VARCHAR(45) NOT NULL, " +
                "email VARCHAR(45) NOT NULL, " +
                "password_hash MEDIUMBLOB NOT NULL, " +
                "salt MEDIUMBLOB NOT NULL, " +
                "mobile INT(11) NOT NULL, " +
                "role VARCHAR(45) NOT NULL, " +
                "PRIMARY KEY (userId)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;";

        // Execute all the create table statements
        stmt.executeUpdate(departmentTable);
        stmt.executeUpdate(disasterTable);
        stmt.executeUpdate(fireTable);
        stmt.executeUpdate(healthTable);
        stmt.executeUpdate(policeTable);
        stmt.executeUpdate(userTable);
    }
}

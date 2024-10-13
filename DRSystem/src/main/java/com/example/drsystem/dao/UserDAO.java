package com.example.drsystem.dao;

import com.example.drsystem.service.*;
import com.example.drsystem.DatabaseConnection;
import com.example.drsystem.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User login(String email, String password) {
        String sql = "SELECT * FROM user WHERE email = ? AND password = ?";
        User user = null; // Initialize the user object

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                // Check if a user was found
                if (resultSet.next()) {
                    user = new User();
                    user.setRole(resultSet.getString("role"));
                    user.setName(resultSet.getString("name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setUserId(resultSet.getInt("userId"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user; // Return the User object or null if not found
    }

    public boolean registerUser(String name, String email, String password, String mobile, String role, String departmentType) {
        String userSql = "INSERT INTO user (name, email, password, mobile, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement userStatement = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {

            userStatement.setString(1, name);
            userStatement.setString(2, email);
            userStatement.setString(3, password);
            userStatement.setString(4, mobile);
            userStatement.setString(5, role);

            int rowsInserted = userStatement.executeUpdate();

            if (rowsInserted > 0 && "DEPARTMENT".equals(role)) {
                // Get the generated user ID
                ResultSet generatedKeys = userStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                    // Insert into department table
                    String departmentSql = "INSERT INTO department (name, email, mobile, userId, departmentType) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement departmentStatement = conn.prepareStatement(departmentSql)) {
                        departmentStatement.setString(1, name);
                        departmentStatement.setString(2, email);
                        departmentStatement.setString(3, mobile);
                        departmentStatement.setInt(4, userId);
                        departmentStatement.setString(5, departmentType);
                        departmentStatement.executeUpdate();
                    }
                }
            }
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getDepartmentTypeByUserId(int userId) {
        String departmentQuery = "SELECT departmentType FROM department WHERE userId = ?";
        String departmentType = null;

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement statement = conn.prepareStatement(departmentQuery)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    departmentType = resultSet.getString("departmentType");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departmentType;
    }

    public User getReportedByUser(int userId) {
        String sql = "SELECT * FROM user WHERE userId = ?";
        User user = null; // Initialize the user object

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                // Check if a user was found
                if (resultSet.next()) {
                    user = new User();
                    user.setRole(resultSet.getString("role"));
                    user.setName(resultSet.getString("name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setUserId(resultSet.getInt("userId"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user; // Return the User object or null if not found
    }

    public boolean deleteUser(int userId) {
        String query = "DELETE FROM user WHERE userId = ?";

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement statement = conn.prepareStatement(query)) {

            // Set the userId in the query
            statement.setInt(1, userId);

            // Execute the delete statement
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while deleting user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public User[] getAllUsers() {
        String query = "SELECT * FROM user";
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement statement = conn.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setMobile(resultSet.getString("mobile"));
                user.setRole(resultSet.getString("role"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while retrieving users: " + e.getMessage());
            e.printStackTrace();
        }

        // Convert the list to an array and return it
        return users.toArray(new User[0]);
    }

    public boolean updateUser(User selectedUser) {
        String query = "UPDATE user SET name = ?, email = ?, mobile = ? WHERE userId = ?";

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, selectedUser.getName());
            statement.setString(2, selectedUser.getEmail());
            statement.setString(3, selectedUser.getMobile());
            statement.setInt(4, selectedUser.getUserId());

            // Execute the update statement
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while deleting user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}

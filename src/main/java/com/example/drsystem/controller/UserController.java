package com.example.drsystem.controller;

import com.example.drsystem.DatabaseConnection;
import com.example.drsystem.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField mobileField;

    @FXML
    private ComboBox roleComboBox;

    private DatabaseConnection databaseConnection;

    static String role;

    ResultSet resultSet;
    User user = new User();
    public UserController() {
        databaseConnection = new DatabaseConnection();
    }

    @FXML
    public boolean login(ActionEvent event) throws SQLException {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (validateLogin(email, password)) {

            user.setRole(resultSet.getString("role"));
            navigateToDashboard(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
        }
        // Logic to authenticate user with the database
        return true; // Example: return true if authenticated
    }

    private boolean validateLogin(String email, String password) {
        Connection connection = databaseConnection.connect();
        String sql = "SELECT * FROM user WHERE email = ? AND password = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true; // Login successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Login failed
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goToRegistration(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/drsystem/registration.fxml"));
            Stage stage = new Stage();
            stage.setTitle("User Registration");
            stage.setScene(new Scene(root, 620, 440));
            stage.show();
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void registerUser(ActionEvent event) throws IOException {
        // Get values from input fields
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String mobile = mobileField.getText();
        String role = roleComboBox.getValue().toString();

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || mobile.isEmpty() || role.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill all fields");
            return;
        }

        // Attempt to save the user to the database
        if (saveUserToDatabase(name, email, password, mobile, role)) {
            user.setRole(role);
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/drsystem/login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("User Registration");
            stage.setScene(new Scene(root, 620, 440));
            stage.show();
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();

        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Failed to register user");
        }
    }

    private boolean saveUserToDatabase(String name, String email, String password, String mobile, String role) {

        String userSql = "INSERT INTO user (name, email, password, mobile, role) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement statement;
        try (Connection conn = databaseConnection.connect()) {
            statement = conn.prepareStatement(userSql);
            // Set parameters for the prepared statement
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, mobile);
            statement.setString(5, role);

            // Execute the update
            int rowsInserted = statement.executeUpdate();
            if (role.equals("DEPARTMENT")) {
                String departmentSql = "INSERT INTO department (name, email, mobile) VALUES (?, ?, ?)";
                statement = conn.prepareStatement(departmentSql);

                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, mobile);

                statement.executeUpdate();
            }

            return rowsInserted > 0; // Return true if at least one row was inserted

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void navigateToDashboard(ActionEvent event) {
        role =user.getRole();
        try {
            Parent root;
            if (user.getRole().equals("ADMIN")){
                root = FXMLLoader.load(getClass().getResource("/com/example/drsystem/admin-dashboard.fxml"));
            } else if (user.getRole().equals("DEPARTMENT")) {
                root = FXMLLoader.load(getClass().getResource("/com/example/drsystem/department-dashboard.fxml"));
            } else {
                root = FXMLLoader.load(getClass().getResource("/com/example/drsystem/user-dashboard.fxml"));
            }
            Stage stage = new Stage();
            stage.setTitle("Disaster Response System Dashboard");
            stage.setScene(new Scene(root, 700, 500)); // Set the desired fixed size
            stage.setResizable(false); // Make the window size fixed
            stage.show();
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();

            // Close the current login window
            Stage loginStage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package com.example.drsystem.controller;

import com.example.drsystem.DrsApplication;
import com.example.drsystem.service.UserService;
import com.example.drsystem.model.User;
import com.example.drsystem.session.UserSession;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
    private ComboBox<String> roleComboBox;

    @FXML
    private ComboBox<String> departmentTypeComboBox;

    @FXML
    private Label departmentTypeLabel;

    private UserService userService = new UserService();

    @FXML
    private void handleRoleSelection() {
        String selectedRole = roleComboBox.getValue();
        if ("DEPARTMENT".equals(selectedRole)) {
            departmentTypeComboBox.setVisible(true);
            departmentTypeLabel.setVisible(true);
        } else {
            departmentTypeComboBox.setVisible(false);
            departmentTypeLabel.setVisible(false);
        }
    }

    @FXML
    public void login(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        Task<User> loginTask = new Task<>() {
            @Override
            protected User call() throws Exception {
                return userService.login(email, password);
            }
        };

        loginTask.setOnSucceeded(e -> {
            User user = loginTask.getValue();
            if (user != null) {
                // Set the user session
                UserSession.getInstance().setLoggedInUser(user);
                navigateToDashboard(event, user);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
            }
        });

        new Thread(loginTask).start();
    }

    @FXML
    private void goToRegistration(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/drsystem/registration.fxml"));
            Stage stage = new Stage();
            stage.setTitle("User Registration");
            Scene scene = new Scene(root);
            stage.setResizable(true);
            stage.setMinWidth(620);
            stage.setMinHeight(440);
            scene.getStylesheets().add(DrsApplication.class.getResource("styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void registerUser(ActionEvent event) {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String mobile = mobileField.getText();
        String role = roleComboBox.getValue();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || mobile.isEmpty() || role.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill all fields");
            return;
        }

        // If the role is DEPARTMENT, check if a department type is selected
        final String departmentType;
        if ("DEPARTMENT".equals(role)) {
            departmentType = departmentTypeComboBox.getValue();
            if (departmentType == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please select a department type.");
                return;
            }
        } else {
            departmentType = null;
        }

        Task<Boolean> registerTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return userService.registerUser(name, email, password, mobile, role, departmentType);  // Use service layer
            }
        };

        registerTask.setOnSucceeded(e -> {
            if (registerTask.getValue()) {
                showAlert(Alert.AlertType.INFORMATION, "Registration Success", "Proceed with the login.");
                navigateToLogin(event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Failed to register user.");
            }
        });

        new Thread(registerTask).start();
    }

    private void navigateToLogin(ActionEvent event) {
        User logUser = UserSession.getInstance().getLoggedInUser();
        if (logUser == null || !logUser.getRole().equals("ADMIN")) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/com/example/drsystem/login.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Login");
                    Scene scene = new Scene(root);
                    stage.setResizable(true);
                    stage.setMinWidth(620);
                    stage.setMinHeight(440);
                    scene.getStylesheets().add(DrsApplication.class.getResource("styles.css").toExternalForm());
                    stage.setScene(scene);
                    stage.show();
                    closeWindow(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private void navigateToDashboard(ActionEvent event, User user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent root;

            // Load the appropriate dashboard based on user role
            switch (user.getRole()) {
                case "ADMIN" ->
                    loader.setLocation(getClass().getResource("/com/example/drsystem/admin-dashboard.fxml"));
                case "DEPARTMENT" -> {
                    setUserSessionDepartment();
                    loader.setLocation(getClass().getResource("/com/example/drsystem/department-dashboard.fxml"));
                }
                default ->
                    loader.setLocation(getClass().getResource("/com/example/drsystem/user-dashboard.fxml"));
            }
            root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Dashboard");
            Scene scene = new Scene(root);
            stage.setResizable(true);
            stage.setMinWidth(620);
            stage.setMinHeight(440);
            scene.getStylesheets().add(DrsApplication.class.getResource("styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

            // Close the current login window
            closeWindow(event);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setUserSessionDepartment() throws SQLException {
        int userId = UserSession.getInstance().getLoggedInUser().getUserId();

        String departmentType = userService.getDepartmentTypeByUserId(userId);

        if (departmentType != null) {
            UserSession.setLoggedUserDepartment(departmentType);
        } else {
            System.err.println("Failed to set department type for user.");
        }
    }

}

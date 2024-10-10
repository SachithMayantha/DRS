package com.example.drsystem.controller;

import com.example.drsystem.DrsApplication;
import com.example.drsystem.model.User;
import com.example.drsystem.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.concurrent.Task;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserManageController {

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, String> usernameColumn;
    
    
    @FXML
    private TableColumn<User, String> userIdColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> mobileColumn;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField mobileField;

    private UserService userService = new UserService();
    private ObservableList<User> usersList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up table columns
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        mobileColumn.setCellValueFactory(new PropertyValueFactory<>("mobile"));

        // Load initial data into the table
        loadUsers();

        // Add listener for table row selection
        usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
    }

    private void loadUsers() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    usersList.clear();
                    usersList.addAll(userService.getAllUsers());
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load users: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                usersTable.setItems(usersList);
            }

            @Override
            protected void failed() {
                super.failed();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load users: " + getException().getMessage());
            }
        };
        new Thread(task).start();
    }

    private void populateFields(User user) {
        usernameField.setText(user.getName());
        emailField.setText(user.getEmail());
        mobileField.setText(user.getMobile());
    }

    @FXML
    public void addUser() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/com/example/drsystem/registration.fxml"));
            Stage stage = new Stage();
            stage.setTitle("User Registration");
            Scene scene = new Scene(root);
            stage.setResizable(true);
            stage.setMinWidth(620);
            stage.setMinHeight(440);
            scene.getStylesheets().add(DrsApplication.class.getResource("styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(UserManageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void updateUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            selectedUser.setName(usernameField.getText());
            selectedUser.setEmail(emailField.getText());
            selectedUser.setMobile(mobileField.getText());

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        userService.updateUser(selectedUser);
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update user: " + e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    loadUsers();
                    clearFields();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully.");
                }
            };

            new Thread(task).start();
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to update.");
        }
    }

    @FXML
    public void deleteUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Show confirmation dialog
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText("Are you sure you want to delete this user?");
            confirmationAlert.setContentText("This action cannot be undone.");

            // Show the alert and wait for a response
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User clicked OK, proceed with deletion
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            userService.deleteUser(selectedUser.getUserId());
                        } catch (SQLException e) {
                            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete user: " + e.getMessage());
                        }
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        loadUsers(); // Refresh the table
                        clearFields();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully.");
                    }
                };

                new Thread(task).start();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to delete.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        usernameField.clear();
        emailField.clear(); 
        mobileField.clear();
    }
}

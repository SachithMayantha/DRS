package com.example.drsystem.controller;

import com.example.drsystem.DrsApplication;
import com.example.drsystem.model.User;
import com.example.drsystem.util.ClientSocketManager;
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

import java.io.IOException;
import java.util.List;
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

    private ClientSocketManager socketManager;
    private ObservableList<User> usersList = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws IOException {
        socketManager = new ClientSocketManager();

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
        try {
            // Requesting the list of users from the server
            List<User> users = (List<User>) socketManager.sendRequest("GET_ALL_USERS", (Object) null);
            usersList.clear();
            usersList.addAll(users);
            usersTable.setItems(usersList);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Connection Error", "Failed to connect to server: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Data Error", "Failed to load user data: " + e.getMessage());
        }
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

            try {
                socketManager.sendRequest("UPDATE_USER", selectedUser);
                loadUsers();
                clearFields();
                showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully.");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update user: " + e.getMessage());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserManageController.class.getName()).log(Level.SEVERE, null, ex);
            }

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
                try {
                    socketManager.sendRequest("DELETE_USER", selectedUser.getUserId());
                    loadUsers(); // Refresh the table
                    clearFields();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully.");
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete user: " + e.getMessage());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(UserManageController.class.getName()).log(Level.SEVERE, null, ex);
                }

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

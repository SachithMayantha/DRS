package com.example.drsystem.controller;

import com.example.drsystem.DatabaseConnection;
import com.example.drsystem.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManageController {
    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private TableColumn<User, String> mobileColumn;

    private ObservableList<User> userList = FXCollections.observableArrayList();

    private DatabaseConnection databaseConnection = new DatabaseConnection();

    public void initialize() {
        // Set up the columns in the table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        mobileColumn.setCellValueFactory(new PropertyValueFactory<>("mobile"));

        // Load data from the database
        loadDataFromDatabase();

        // Set the items for the TableView
        userTable.setItems(userList);
    }

    private void loadDataFromDatabase() {
        String query = "SELECT * FROM user";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String role = resultSet.getString("role");
                String mobile = resultSet.getString("mobile");

                User user = new User(id, name, email, role, mobile);
                userList.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Logic to edit the user
            // You might open a new window or a form to update the user's details
            showAlert(Alert.AlertType.INFORMATION, "Edit User", "Edit functionality not implemented.");
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to edit.");
        }
    }

    @FXML
    private void deleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                Connection conn = databaseConnection.connect();
                String query = "DELETE FROM user WHERE id = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, selectedUser.getUserId()
                );
                preparedStatement.executeUpdate();

                userList.remove(selectedUser);
                showAlert(Alert.AlertType.INFORMATION, "Delete User", "User deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete user.");
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
}

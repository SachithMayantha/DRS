package com.example.drsystem.controller;

import com.example.drsystem.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PoliceController {

    @FXML
    private TextField policemanField;

    @FXML
    private TextField controllerField;

    @FXML
    private TextField investigatorField;

    @FXML
    private ComboBox<String> statusComboBox;

    private DatabaseConnection databaseConnection;

    public PoliceController() {
        databaseConnection = new DatabaseConnection();
    }

    // hold the selected disaster ID
    private static int disasterId;

    public void initialize() {
        statusComboBox.setValue("Pending"); // Default status
    }

    @FXML
    public void policeResourceAllocation(ActionEvent event) {

        String policeman = policemanField.getText();
        String trafficController = controllerField.getText();
        String investigators = investigatorField.getText();
        String status = statusComboBox.getValue();

        // Validate input fields
        if (policeman.isEmpty() || trafficController.isEmpty() || investigators.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all the fields.");
            return;
        }

        try {
            // Parse the input to integers
            int policemanCount = Integer.parseInt(policeman);
            int controllerCount = Integer.parseInt(trafficController);
            int investigatorCount = Integer.parseInt(investigators);

            disasterId = DisasterTableController.selectedDisasterId;
            // Save resource allocation to the database
            savePoliceAllocation(disasterId, policemanCount, controllerCount, investigatorCount, status);

            // Close the allocation window after saving
            Stage stage = (Stage) policemanField.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numbers for resources.");
        }
    }

    private void savePoliceAllocation(int disasterId, int policeman, int trafficControllers, int investigators, String status) {
        String query = "INSERT INTO police (disasterId, policeman, trafficControllers, investigators, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, disasterId); // Foreign key to the disaster
            stmt.setInt(2, policeman);
            stmt.setInt(3, trafficControllers);
            stmt.setInt(4, investigators);
            stmt.setString(5, status);

            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Health resources allocated successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to allocate resources.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

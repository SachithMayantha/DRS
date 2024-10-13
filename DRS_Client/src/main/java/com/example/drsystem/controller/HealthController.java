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

public class HealthController {

    @FXML
    private TextField doctorsField;

    @FXML
    private TextField nursesField;

    @FXML
    private TextField ambulanceField;

    @FXML
    private ComboBox<String> statusComboBox;

    private DatabaseConnection databaseConnection;

    public HealthController() {
        databaseConnection = new DatabaseConnection();
    }

    // hold the selected disaster ID
    private static int disasterId;

    public void initialize() {
        statusComboBox.setValue("Pending"); // Default status
    }

    @FXML
    public void healthResourceAllocation() {

        String doctors = doctorsField.getText();
        String nurses = nursesField.getText();
        String ambulances = ambulanceField.getText();
        String status = statusComboBox.getValue();

        // Validate input fields
        if (doctors.isEmpty() || nurses.isEmpty() || ambulances.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all the fields.");
            return;
        }

        try {
            // Parse the input to integers
            int doctorCount = Integer.parseInt(doctors);
            int nurseCount = Integer.parseInt(nurses);
            int ambulanceCount = Integer.parseInt(ambulances);

            disasterId = DisasterTableController.selectedDisasterId;

            // Save resource allocation to the database
            saveHealthAllocation(disasterId, doctorCount, nurseCount, ambulanceCount, status);

            // Close the allocation window after saving
            Stage stage = (Stage) doctorsField.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numbers for resources.");
        }
    }

    private void saveHealthAllocation(int disasterId, int doctors, int nurses, int ambulances, String status) {
        String query = "INSERT INTO health (disasterId, doctors, nurses, ambulances, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, disasterId); // Foreign key to the disaster
            stmt.setInt(2, doctors);
            stmt.setInt(3, nurses);
            stmt.setInt(4, ambulances);
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

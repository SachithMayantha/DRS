package com.example.drsystem.controller;

import com.example.drsystem.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DisasterController {

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private TextField locationField;

    @FXML
    private ComboBox<String> locationTypeComboBox;

    @FXML
    private TextField descriptionField;

    @FXML
    private ComboBox<String> severityComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label imageLabel;

    private File selectedImageFile;

    @FXML
    private TextField reportedByField;

    private DatabaseConnection databaseConnection;

    public DisasterController() {
        databaseConnection = new DatabaseConnection();
    }

    @FXML
    private void submitDisasterReport(ActionEvent event) {
        String type = typeComboBox.getValue();
        String location = locationField.getText();
        String locationType = locationTypeComboBox.getValue();
        String description = descriptionField.getText();
        String severity = severityComboBox.getValue();
        java.sql.Date date = java.sql.Date.valueOf(datePicker.getValue());
        String reportedBy = reportedByField.getText();

        if (type == null || location.isEmpty() || description.isEmpty() || severity.isEmpty() || date == null || reportedBy.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill all fields");
            return;
        }

        if (saveDisasterToDatabase(type, location, locationType, description, severity, date, reportedBy)) {
            showAlert(Alert.AlertType.INFORMATION, "Success!", "Disaster reported successfully");
            Stage reportStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            reportStage.close();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to report disaster");
        }
    }

    private boolean saveDisasterToDatabase(String type, String location, String locationType, String description,
                                           String severity, java.sql.Date date, String reportedBy) {
        //quick and accurate assessment of the reported disasters and providing prioritized number
        int priorityNo = AssessmentController.assessDisaster(type, locationType,severity);

        String sql = "INSERT INTO disaster (type, location, description, severity, date, reportedBy, locationType, priorityNo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type);
            stmt.setString(2, location);
            stmt.setString(3, description);
            stmt.setString(4, severity);
            stmt.setDate(5, date);
            stmt.setString(6, reportedBy);
            stmt.setString(7, locationType);
            stmt.setInt(8, priorityNo);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; // Return true if at least one row was inserted

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        Stage stage = (Stage) imageLabel.getScene().getWindow(); // Get the current window
        selectedImageFile = fileChooser.showOpenDialog(stage);

        if (selectedImageFile != null) {
            imageLabel.setText(selectedImageFile.getName());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

package com.example.drsystem.controller;

import com.example.drsystem.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentController {

    @FXML
    private Label disasterIdLabel;

    @FXML
    private Label doctorsLabel;

    @FXML
    private Label nursesLabel;

    @FXML
    private Label ambulancesLabel;

    @FXML
    private Label policemanLabel;

    @FXML
    private Label controllerLabel;

    @FXML
    private Label investigatorLabel;

    @FXML
    private Label fighterLabel;

    @FXML
    private Label supporterLabel;

    @FXML
    private Label suppressionLabel;

    @FXML
    private ComboBox<String> statusComboBox;

    private static int disasterId;

    private DatabaseConnection databaseConnection;

    public DepartmentController(){
        databaseConnection = new DatabaseConnection();
    }
    
    public void initialize(){
        disasterId = DisasterTableController.selectedDisasterId;
        getHealthResources(disasterId);
        getFireResources(disasterId);
        getPoliceResources(disasterId);
    }

    private void getPoliceResources(int disasterId) {
        String query = "SELECT policeman, trafficControllers, " +
                "investigators, status FROM health WHERE disasterId = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the disasterId in the query
            stmt.setInt(1, disasterId);

            // Execute the query and process the result set
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Get the requested values from the database
                    int policeman = rs.getInt("policeman");
                    int trafficControllers = rs.getInt("trafficControllers");
                    int investigators = rs.getInt("investigators");
                    String status = rs.getString("status");

                    // Set the values to the labels and the status ComboBox
                    disasterIdLabel.setText(String.valueOf(disasterId));
                    policemanLabel.setText(String.valueOf(policeman));
                    controllerLabel.setText(String.valueOf(trafficControllers));
                    investigatorLabel.setText(String.valueOf(investigators));
                    statusComboBox.setValue(status);
                } else {
                    showAlert(Alert.AlertType.
                            INFORMATION, "No Data", "No police resource data found for this disaster.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.
                    ERROR, "Database Error", "Failed to retrieve police resource data.");
        }
    }

    public void getHealthResources(int disasterId) {

        String query = "SELECT doctors, nurses, ambulances, status FROM health WHERE disasterId = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the disasterId in the query
            stmt.setInt(1, disasterId);

            // Execute the query and process the result set
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Get the requested values from the database
                    int doctors = rs.getInt("doctors");
                    int nurses = rs.getInt("nurses");
                    int ambulances = rs.getInt("ambulances");
                    String status = rs.getString("status");

                    // Set the values to the labels and the status ComboBox
                    disasterIdLabel.setText(String.valueOf(disasterId));
                    doctorsLabel.setText(String.valueOf(doctors));
                    nursesLabel.setText(String.valueOf(nurses));
                    ambulancesLabel.setText(String.valueOf(ambulances));
                    statusComboBox.setValue(status);
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "No Data", "No health resource data found for this disaster.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve health resource data.");
        }
    }

    public void getFireResources(int disasterId) {

        String query = "SELECT fighters, " +
                "supporters, suppression, status FROM fire WHERE disasterId = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the disasterId in the query
            stmt.setInt(1, disasterId);

            // Execute the query and process the result set
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Get the requested values from the database
                    int fighters = rs.getInt("fighters");
                    int supporters = rs.getInt("supporters");
                    int suppression = rs.getInt("suppression");
                    String status = rs.getString("status");

                    // Set the values to the labels and the status ComboBox
                    disasterIdLabel.setText(String.valueOf(disasterId));
                    fighterLabel.setText(String.valueOf(fighters));
                    supporterLabel.setText(String.valueOf(supporters));
                    suppressionLabel.setText(String.valueOf(suppression));
                    statusComboBox.setValue(status);
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "No Data", "No fire resource data found for this disaster.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve fire resource data.");
        }
    }


    @FXML
    private void saveHealthResourceStatus() {
        String status = statusComboBox.getValue();

        String query = "UPDATE health SET status = ? WHERE disasterId = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setInt(2, disasterId);
            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Health resource status updated successfully.");

            // Close the current window
            Stage stage = (Stage) statusComboBox.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update status.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void saveFireResourceStatus() {
        String status = statusComboBox.getValue();

        String query = "UPDATE fire SET status = ? WHERE disasterId = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setInt(2, disasterId);
            stmt.executeUpdate();

            showAlert(Alert.AlertType
                    .INFORMATION, "Success", "Fire Emergency resource status updated successfully.");

            // Close the current window
            Stage stage = (Stage) statusComboBox.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType
                    .ERROR, "Database Error", "Failed to update status.");
        }
    }

    public void savePoliceResourceStatus() {
        String status = statusComboBox.getValue();

        String query = "UPDATE police SET status = ? WHERE disasterId = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setInt(2, disasterId);
            stmt.executeUpdate();

            showAlert(Alert.AlertType
                    .INFORMATION, "Success", "Police Department resource status updated successfully.");

            // Close the current window
            Stage stage = (Stage) statusComboBox.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update status.");
        }
    }
}

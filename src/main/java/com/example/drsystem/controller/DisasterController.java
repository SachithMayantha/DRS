package com.example.drsystem.controller;

import com.example.drsystem.model.User;
import com.example.drsystem.session.UserSession;
import com.example.drsystem.util.ClientSocketManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DisasterController {

    @FXML
    public ComboBox<String> typeComboBox, locationTypeComboBox, severityComboBox;

    @FXML
    public TextField locationField;

    @FXML
    public TextArea descriptionField;

    @FXML
    public DatePicker datePicker;

    @FXML
    public Label imageLabel, reportedByLabel; // To display reporting user info

    @FXML
    public ImageView imageView;

    public File selectedImageFile;

    public final User loggedInUser = UserSession.getInstance().getLoggedInUser(); // Get logged-in user

    private ClientSocketManager socket;

    @FXML
    public void initialize() throws IOException {
        socket = new ClientSocketManager();
        if (loggedInUser != null) {
            System.out.println(loggedInUser.toString());
            reportedByLabel.setText(loggedInUser.getName() + " (Email: " + loggedInUser.getEmail() + ")");
        } else {
            reportedByLabel.setText("No user logged in");
        }
    }

    @FXML
    public void submitDisasterReport(ActionEvent event) {
        if (isInputValid()) {
            String type = typeComboBox.getValue();
            String location = locationField.getText();
            String locationType = locationTypeComboBox.getValue();
            String description = descriptionField.getText();
            String severity = severityComboBox.getValue();
            Date date = Date.valueOf(datePicker.getValue());
            String reportedBy = String.valueOf(loggedInUser.getUserId());

            byte[] imageBytes = selectedImageFile != null ? convertImageToByteArray(selectedImageFile) : null;

            int priorityNo = AssessmentController.assessDisaster(type, locationType, severity);
            Boolean result;
            try {
                result = (Boolean) socket.sendRequest("SAVE_DISASTER", type, location, locationType, description, severity, date, reportedBy, priorityNo, imageBytes);
                if (result) {
                    showAlert(Alert.AlertType.INFORMATION, "Success!", "Disaster reported successfully");
                    ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to report disaster");
                }
            } catch (IOException ex) {
                Logger.getLogger(DisasterController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DisasterController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @FXML
    public void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        selectedImageFile = fileChooser.showOpenDialog(imageLabel.getScene().getWindow());

        if (selectedImageFile != null) {
            imageLabel.setText(selectedImageFile.getName());
            imageView.setImage(new Image(selectedImageFile.toURI().toString()));
        }
    }

    // Helper methods
    private boolean isInputValid() {
        if (typeComboBox.getValue() == null || locationField.getText().isEmpty()
                || descriptionField.getText().isEmpty() || severityComboBox.getValue().isEmpty()
                || datePicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill all fields");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private byte[] convertImageToByteArray(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

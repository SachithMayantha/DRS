package com.example.drsystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    // navigate report disaster user interface
    @FXML
    private void reportDisaster() throws IOException {
        Parent root = FXMLLoader.load(getClass()
                .getResource("/com/example/drsystem/report-disaster.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Report Disaster");
        stage.setScene(new Scene(root, 620, 440));
        stage.show();
    }

    @FXML
    private void viewAdminDisasters() throws IOException {
        Parent root = FXMLLoader.load(getClass()
                .getResource("/com/example/drsystem/admin-disaster-table.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Disaster Table");
        stage.setScene(new Scene(root, 620, 440));
        stage.show();
    }

    @FXML
    private void viewUserDisasters() throws IOException {
        Parent root = FXMLLoader.load(getClass()
                .getResource("/com/example/drsystem/user-disasters-table.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Disaster Table");
        stage.setScene(new Scene(root, 620, 440));
        stage.show();
    }


    @FXML
    private void logout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/com/example/drsystem/login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root, 620, 440)); // Set the desired size for the login window
            stage.setResizable(false); // Make the window size fixed
            stage.show();

            // Close the current dashboard window
            Stage dashboardStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            dashboardStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        @FXML
        private void viewFAQ() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/com/example/drsystem/faq_view.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Frequently Asked Questions");
                stage.setScene(new Scene(root, 620, 440));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    @FXML
    private void goBackToDashboard(ActionEvent event) {
        // Close the current dashboard window
        Stage dashboardStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        dashboardStage.close();
    }
}

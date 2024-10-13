package com.example.drsystem.controller;

import com.example.drsystem.model.Disaster;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DisasterManageController {
    @FXML
    private Label disasterIdLabel;

    @FXML
    private Label disasterTypeLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label locationTypeLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label severityLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label reportedByLabel;

    @FXML
    private Label priorityNoLabel;

    public void setDisasterDetails(Disaster disaster) {
        disasterIdLabel.setText(String.valueOf(disaster.getDisasterId()));
        disasterTypeLabel.setText(disaster.getType());
        locationLabel.setText(disaster.getLocation());
        locationTypeLabel.setText(disaster.getLocationType());
        descriptionLabel.setText(disaster.getDescription());
        severityLabel.setText(disaster.getSeverity());
        dateLabel.setText(disaster.getDate().toString());
        reportedByLabel.setText(disaster.getReportedBy());
        priorityNoLabel.setText(String.valueOf(disaster.getPriorityNo()));
    }

    @FXML
    public void resourceAllocationPolice() throws IOException {
        Parent root = FXMLLoader.load(getClass().
                getResource("/com/example/drsystem/police_resource_allocation.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Police Department");
        stage.setScene(new Scene(root, 620, 440));
        stage.show();
    }

    @FXML
    public void resourceAllocationHealth() throws IOException {
        Parent root = FXMLLoader.load(getClass().
                getResource("/com/example/drsystem/health_resource_allocation.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Health Department");
        stage.setScene(new Scene(root, 620, 440));
        stage.show();
    }

    @FXML
    public void resourceAllocationFire() throws IOException {
        Parent root = FXMLLoader.load(getClass().
                getResource("/com/example/drsystem/fire_resource_allocation.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Fire Emergency");
        stage.setScene(new Scene(root, 620, 440));
        stage.show();
    }

    @FXML
    public void showHealthResources() throws IOException {
        Parent root = FXMLLoader.load(getClass().
                getResource("/com/example/drsystem/health_resources_view.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Health Department");
        stage.setScene(new Scene(root, 620, 440));
        stage.show();
    }

    public void showFireResources() throws IOException {
        Parent root = FXMLLoader.load(getClass().
                getResource("/com/example/drsystem/fire_resources_view.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Fire Emergency");
        stage.setScene(new Scene(root, 620, 440));
        stage.show();
    }

    public void showPoliceResources() throws IOException {
        Parent root = FXMLLoader.load(getClass().
                getResource("/com/example/drsystem/police_resources_view.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Police Department");
        stage.setScene(new Scene(root, 620, 440));
        stage.show();
    }
}

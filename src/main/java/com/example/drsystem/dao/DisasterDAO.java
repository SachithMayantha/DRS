package com.example.drsystem.dao;

import com.example.drsystem.DatabaseConnection;
import com.example.drsystem.model.Disaster;
import com.example.drsystem.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisasterDAO {

    // Save disaster to database
    public boolean saveDisasterToDatabase(String type, String location, String locationType, String description, String severity,
                                          java.sql.Date date, String reportedBy, int priorityNo, byte[] image) throws SQLException {
        String sql = "INSERT INTO disaster (type, location, locationType, description, severity, date, reportedBy, priorityNo, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, type);
            statement.setString(2, location);
            statement.setString(3, locationType);
            statement.setString(4, description);
            statement.setString(5, severity);
            statement.setDate(6, date);
            statement.setString(7, reportedBy);
            statement.setInt(8, priorityNo);
            statement.setBytes(9, image);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        }
    }

    // Retrieve all disasters
    public List<Disaster> getAllDisasters() throws SQLException {
        List<Disaster> disasterList = new ArrayList<>();
        String query = "SELECT * FROM disaster";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Disaster disaster = mapResultSetToDisaster(resultSet);
                disasterList.add(disaster);
            }
        }
        return disasterList;
    }

    // Retrieve disasters reported by a specific user
    public List<Disaster> getUserDisasters(int userId) throws SQLException {
        List<Disaster> disasterList = new ArrayList<>();
        String query = "SELECT * FROM disaster WHERE reportedBy = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Disaster disaster = mapResultSetToDisaster(resultSet);
                    disasterList.add(disaster);
                }
            }
        }
        return disasterList;
    }

    // Retrieve department-specific disasters
    public List<Disaster> getDepartmentDisasters(String departmentType) throws SQLException {
        List<Disaster> disasterList = new ArrayList<>();
        String departmentQuery ="";
        
        switch (departmentType) {
            case "FIRE" ->
                departmentQuery = "SELECT d.* FROM drs.disaster d JOIN fire f ON d.disasterId = f.disasterId";
            case "HEALTH" ->
                departmentQuery = "SELECT d.* FROM drs.disaster d JOIN health h ON d.disasterId = h.disasterId";
            case "POLICE" ->
                departmentQuery = "SELECT d.* FROM drs.disaster d JOIN police p ON d.disasterId = p.disasterId";            
        }

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement statement = conn.prepareStatement(departmentQuery);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Disaster disaster = mapResultSetToDisaster(resultSet);
                disasterList.add(disaster);
            }
        }
        return disasterList;
    }

    // Map ResultSet to Disaster object
    private Disaster mapResultSetToDisaster(ResultSet resultSet) throws SQLException {
        int disasterId = resultSet.getInt("disasterId");
        String type = resultSet.getString("type");
        String location = resultSet.getString("location");
        String locationType = resultSet.getString("locationType");
        String description = resultSet.getString("description");
        String severity = resultSet.getString("severity");
        java.sql.Date date = resultSet.getDate("date");
        User reportedBy = (new UserDAO()).getReportedByUser(resultSet.getInt("reportedBy"));
        int priorityNo = resultSet.getInt("priorityNo");
        byte[] image = resultSet.getBytes("image");

        return new Disaster(disasterId, type, location, locationType, description, severity,
                date.toLocalDate(), reportedBy, priorityNo, image);
    }
}

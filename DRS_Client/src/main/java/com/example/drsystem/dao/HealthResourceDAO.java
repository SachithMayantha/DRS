package com.example.drsystem.dao;

import com.example.drsystem.DatabaseConnection;
import com.example.drsystem.model.HealthResource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HealthResourceDAO {

    public void saveHealthAllocation(int disasterId, int doctors, int nurses, int ambulances, String status) throws SQLException {
        String query = "INSERT INTO health (disasterId, doctors, nurses, ambulances, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, disasterId); // Foreign key to the disaster
            stmt.setInt(2, doctors);
            stmt.setInt(3, nurses);
            stmt.setInt(4, ambulances);
            stmt.setString(5, status);

            stmt.executeUpdate();
        }
    }

    public HealthResource getHealthResources(int disasterId) throws SQLException{
        String query = "SELECT healthId, doctors, nurses, ambulances, status FROM health WHERE disasterId = ?";

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, disasterId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    HealthResource healthResource = new HealthResource(rs.getInt("healthId"), disasterId, rs.getInt("doctors"), rs.getInt("nurses"), rs.getInt("ambulances"), rs.getString("status"));

                    return healthResource;

                }
            }
        }
        return null;
    }

    public void saveHealthResourceStatus(int disasterId, String status) throws SQLException {
        String query = "UPDATE health SET status = ? WHERE disasterId = ?";
        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, disasterId);
            stmt.executeUpdate();
        } 
    }

}

package com.example.drsystem.dao;

import com.example.drsystem.service.*;
import com.example.drsystem.DatabaseConnection;
import com.example.drsystem.model.HealthResource;
import com.example.drsystem.model.PoliceResource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PoliceResourceDAO {

   public void savePoliceAllocation(int disasterId, int policeman, int trafficControllers, int investigators, String status) throws SQLException {
        String query = "INSERT INTO police (disasterId, policeman, trafficControllers, investigators, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, disasterId); // Foreign key to the disaster
            stmt.setInt(2, policeman);
            stmt.setInt(3, trafficControllers);
            stmt.setInt(4, investigators);
            stmt.setString(5, status);

            stmt.executeUpdate();
        }
    }
    
     public PoliceResource getPoliceResources(int disasterId) throws SQLException{
        String query = "SELECT policeId, policeman, trafficControllers, " +
                "investigators, status FROM police WHERE disasterId = ?";

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, disasterId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    PoliceResource policeResource = new PoliceResource(rs.getInt("policeId"), disasterId, rs.getInt("policeman"), rs.getInt("trafficControllers"), rs.getInt("investigators"), rs.getString("status"));

                    return policeResource;

                }
            }
        }
        return null;
    }

    public void savePoliceResourceStatus(int disasterId, String status) throws SQLException {
        String query = "UPDATE police SET status = ? WHERE disasterId = ?";
        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, disasterId);
            stmt.executeUpdate();
        } 
    }
}

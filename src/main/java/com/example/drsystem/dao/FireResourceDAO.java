package com.example.drsystem.dao;

import com.example.drsystem.DatabaseConnection;
import com.example.drsystem.model.FireResource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FireResourceDAO {

    public void saveHealthAllocation(int disasterId, int fighters, int supporters, int suppression, String status) throws SQLException {
        String query = "INSERT INTO fire (disasterId, fighters, supporters, suppression, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, disasterId); // Foreign key to the disaster
            stmt.setInt(2, fighters);
            stmt.setInt(3, supporters);
            stmt.setInt(4, suppression);
            stmt.setString(5, status);
            stmt.executeUpdate();
        }
    }
    
    public FireResource getFireResources(int disasterId) throws SQLException{
        String query = "SELECT fireId, fighters, " +
                "supporters, suppression, status FROM fire WHERE disasterId = ?";

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, disasterId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    FireResource fireResource = new FireResource(rs.getInt("fireId"), disasterId, rs.getInt("fighters"), rs.getInt("supporters"), rs.getInt("suppression"), rs.getString("status"));

                    return fireResource;

                }
            }
        }
        return null;
    }

    public void saveFireResourceStatus(int disasterId, String status) throws SQLException {
        String query = "UPDATE fire SET status = ? WHERE disasterId = ?";
        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, disasterId);
            stmt.executeUpdate();
        } 
    }
}

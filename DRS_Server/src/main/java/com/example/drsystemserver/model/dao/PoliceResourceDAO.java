package com.example.drsystemserver.model.dao;

import com.example.drsystemserver.util.DatabaseConnection;
import com.example.drsystem.model.PoliceResource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ashan
 * Police Resource DAO
 */
public class PoliceResourceDAO {

    public Boolean savePoliceAllocation(int disasterId, int policeman, int trafficControllers, int investigators, String status) throws SQLException {
        String query = "INSERT INTO police (disasterId, policeman, trafficControllers, investigators, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, disasterId);
            stmt.setInt(2, policeman);
            stmt.setInt(3, trafficControllers);
            stmt.setInt(4, investigators);
            stmt.setString(5, status);

            // Execute the update and check if any rows were affected
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if at least one row was updated, false otherwise
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception if needed
        }
    }

    public PoliceResource getPoliceResources(int disasterId) throws SQLException {
        String query = "SELECT policeId, policeman, trafficControllers, "
                + "investigators, status FROM police WHERE disasterId = ?";

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

    public Boolean savePoliceResourceStatus(int disasterId, String status) throws SQLException {
        String query = "UPDATE police SET status = ? WHERE disasterId = ?";
        try (Connection conn = DatabaseConnection.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, disasterId);
            int res = stmt.executeUpdate();
            return res > 0;
        }
    }
}

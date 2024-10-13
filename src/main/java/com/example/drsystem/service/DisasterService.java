package com.example.drsystem.service;

import com.example.drsystem.model.Disaster;
import com.example.drsystem.dao.DisasterDAO;

import java.sql.SQLException;
import java.util.List;

public class DisasterService {

    private final DisasterDAO disasterDAO;

    public DisasterService() {
        this.disasterDAO = new DisasterDAO();
    }

    // Save disaster report
    public boolean saveDisaster(String type, String location, String locationType, String description, String severity,
            java.sql.Date date, String reportedBy, int priorityNo, byte[] image) throws SQLException {
        return disasterDAO.saveDisasterToDatabase(type, location, locationType, description, severity, date, reportedBy, priorityNo, image);
    }

    // Retrieve all disasters
    public List<Disaster> getAllDisasters() throws SQLException {
        return disasterDAO.getAllDisasters();
    }

    // Retrieve disasters reported by a specific user
    public List<Disaster> getUserDisasters(int userId) throws SQLException {
        return disasterDAO.getUserDisasters(userId);
    }

    // Retrieve department-specific disasters based on a department query
    public List<Disaster> getDepartmentDisasters(String departmentType) throws SQLException {
        return disasterDAO.getDepartmentDisasters(departmentType);
    }
}

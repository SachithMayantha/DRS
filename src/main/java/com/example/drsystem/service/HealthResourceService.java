package com.example.drsystem.service;

import com.example.drsystem.dao.HealthResourceDAO;
import com.example.drsystem.model.HealthResource;

import java.sql.SQLException;

public class HealthResourceService {

    private final HealthResourceDAO fireResourceDAO;

    public HealthResourceService() {
        this.fireResourceDAO = new HealthResourceDAO();
    }

    public void allocateHealthResources(int disasterId, int fighters, int supporters, int suppression, String status) throws SQLException {
        fireResourceDAO.saveHealthAllocation(disasterId, fighters, supporters, suppression, status);
    }

    public HealthResource getHealthResources(int disasterId) throws SQLException {
        return fireResourceDAO.getHealthResources(disasterId);
    }

    public void updateHealthResourceStatus(int disasterId, String status) throws SQLException {
        fireResourceDAO.saveHealthResourceStatus(disasterId, status);
    }
}

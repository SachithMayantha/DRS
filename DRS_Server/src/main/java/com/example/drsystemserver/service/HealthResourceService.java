package com.example.drsystemserver.service;

import com.example.drsystemserver.model.dao.HealthResourceDAO;
import com.example.drsystem.model.HealthResource;

import java.sql.SQLException;

/**
 * @author jayath
 * Health Resources Service for update and get resource details
 */
public class HealthResourceService {

    private final HealthResourceDAO fireResourceDAO;

    public HealthResourceService() {
        this.fireResourceDAO = new HealthResourceDAO();
    }

    public HealthResourceService(HealthResourceDAO fireResourceDAO) {
        this.fireResourceDAO = fireResourceDAO;
    }

    public Boolean allocateHealthResources(int disasterId, int doctors, int nurses, int ambulance, String status) throws SQLException {
        return fireResourceDAO.saveHealthAllocation(disasterId, doctors, nurses, ambulance, status);
    }

    public HealthResource getHealthResources(int disasterId) throws SQLException {
        return fireResourceDAO.getHealthResources(disasterId);
    }

    public Boolean updateHealthResourceStatus(int disasterId, String status) throws SQLException {
        return fireResourceDAO.saveHealthResourceStatus(disasterId, status);
    }
}

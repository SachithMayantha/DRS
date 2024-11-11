package com.example.drsystemserver.service;

import com.example.drsystemserver.model.dao.PoliceResourceDAO;
import com.example.drsystem.model.PoliceResource;

import java.sql.SQLException;

/**
 * @author jayath
 * Police Resources Service for update and get resource details
 */
public class PoliceResourceService {

    private final PoliceResourceDAO fireResourceDAO;

    public PoliceResourceService() {
        this.fireResourceDAO = new PoliceResourceDAO();
    }

    public PoliceResourceService(PoliceResourceDAO fireResourceDAO) {
        this.fireResourceDAO = fireResourceDAO;
    }
    
    public Boolean allocatePoliceResources(int disasterId, int fighters, int supporters, int suppression, String status) throws SQLException {
        return fireResourceDAO.savePoliceAllocation(disasterId, fighters, supporters, suppression, status);
    }

    public PoliceResource getPoliceResources(int disasterId) throws SQLException {
        return fireResourceDAO.getPoliceResources(disasterId);
    }

    public Boolean updatePoliceResourceStatus(int disasterId, String status) throws SQLException {
        return fireResourceDAO.savePoliceResourceStatus(disasterId, status);
    }
}

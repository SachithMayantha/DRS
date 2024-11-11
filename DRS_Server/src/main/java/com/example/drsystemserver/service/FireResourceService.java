package com.example.drsystemserver.service;

import com.example.drsystemserver.model.dao.FireResourceDAO;
import com.example.drsystem.model.FireResource;

import java.sql.SQLException;

/**
 * @author jayath
 * Fire Resources Service for update and get resource details
 */
public class FireResourceService {

    private final FireResourceDAO fireResourceDAO;

    public FireResourceService() {
        this.fireResourceDAO = new FireResourceDAO();
    }

    public FireResourceService(FireResourceDAO fireResourceDAO) {
        this.fireResourceDAO = fireResourceDAO;
    }

    public Boolean allocateFireResources(int disasterId, int fighters, int supporters, int suppression, String status) throws SQLException {
        return fireResourceDAO.saveFireAllocation(disasterId, fighters, supporters, suppression, status);
    }

    public FireResource getFireResources(int disasterId) throws SQLException {
        return fireResourceDAO.getFireResources(disasterId);
    }

    public Boolean updateFireResourceStatus(int disasterId, String status) throws SQLException {
        return fireResourceDAO.saveFireResourceStatus(disasterId, status);
    }
}

package com.example.drsystemserver.service;

import com.example.drsystem.model.HealthResource;
import com.example.drsystemserver.model.dao.HealthResourceDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HealthResourceServiceTest {

    @Mock
    private HealthResourceDAO healthResourceDAOMock;

    private HealthResourceService healthResourceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        healthResourceService = new HealthResourceService(healthResourceDAOMock);
    }

    @Test
    void testAllocateHealthResourcesSuccessful() throws SQLException {
        int disasterId = 1;
        int doctors = 5;
        int nurses = 10;
        int ambulance = 2;
        String status = "Allocated";

        when(healthResourceDAOMock.saveHealthAllocation(disasterId, doctors, nurses, ambulance, status)).thenReturn(true);

        Boolean result = healthResourceService.allocateHealthResources(disasterId, doctors, nurses, ambulance, status);
        assertTrue(result);
    }

    @Test
    void testAllocateHealthResourcesFailure() throws SQLException {
        int disasterId = 1;
        int doctors = 5;
        int nurses = 10;
        int ambulance = 2;
        String status = "Failed";

        when(healthResourceDAOMock.saveHealthAllocation(disasterId, doctors, nurses, ambulance, status)).thenReturn(false);

        Boolean result = healthResourceService.allocateHealthResources(disasterId, doctors, nurses, ambulance, status);
        assertFalse(result);
    }

    @Test
    void testAllocateHealthResourcesThrowsSQLException() throws SQLException {
        int disasterId = 1;
        int doctors = 5;
        int nurses = 10;
        int ambulance = 2;
        String status = "Error";

        when(healthResourceDAOMock.saveHealthAllocation(disasterId, doctors, nurses, ambulance, status))
                .thenThrow(new SQLException("Database error"));

        SQLException thrown = assertThrows(SQLException.class, () -> {
            healthResourceService.allocateHealthResources(disasterId, doctors, nurses, ambulance, status);
        });

        assertEquals("Database error", thrown.getMessage());
    }

    @Test
    void testGetHealthResourcesSuccessful() throws SQLException {
        int disasterId = 1;
        HealthResource expectedResource = new HealthResource(); // Initialize with specific values as necessary

        when(healthResourceDAOMock.getHealthResources(disasterId)).thenReturn(expectedResource);

        HealthResource result = healthResourceService.getHealthResources(disasterId);
        assertEquals(expectedResource, result);
    }

    @Test
    void testGetHealthResourcesThrowsSQLException() throws SQLException {
        int disasterId = 1;

        when(healthResourceDAOMock.getHealthResources(disasterId)).thenThrow(new SQLException("Database error"));

        SQLException thrown = assertThrows(SQLException.class, () -> {
            healthResourceService.getHealthResources(disasterId);
        });

        assertEquals("Database error", thrown.getMessage());
    }

    @Test
    void testUpdateHealthResourceStatusSuccessful() throws SQLException {
        int disasterId = 1;
        String status = "Updated";

        when(healthResourceDAOMock.saveHealthResourceStatus(disasterId, status)).thenReturn(true);

        Boolean result = healthResourceService.updateHealthResourceStatus(disasterId, status);
        assertTrue(result);
    }

    @Test
    void testUpdateHealthResourceStatusFailure() throws SQLException {
        int disasterId = 1;
        String status = "Failed";

        when(healthResourceDAOMock.saveHealthResourceStatus(disasterId, status)).thenReturn(false);

        Boolean result = healthResourceService.updateHealthResourceStatus(disasterId, status);
        assertFalse(result);
    }

    @Test
    void testUpdateHealthResourceStatusThrowsSQLException() throws SQLException {
        int disasterId = 1;
        String status = "Error";

        when(healthResourceDAOMock.saveHealthResourceStatus(disasterId, status))
                .thenThrow(new SQLException("Database error"));

        SQLException thrown = assertThrows(SQLException.class, () -> {
            healthResourceService.updateHealthResourceStatus(disasterId, status);
        });

        assertEquals("Database error", thrown.getMessage());
    }
}

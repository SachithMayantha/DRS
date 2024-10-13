package com.example.drsystemserver.service;

import com.example.drsystem.model.PoliceResource;
import com.example.drsystemserver.model.dao.PoliceResourceDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PoliceResourceServiceTest {

    @Mock
    private PoliceResourceDAO policeResourceDAOMock;

    private PoliceResourceService policeResourceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        policeResourceService = new PoliceResourceService(policeResourceDAOMock);
    }

    @Test
    void testAllocatePoliceResourcesSuccessful() throws SQLException {
        int disasterId = 1;
        int fighters = 5;
        int supporters = 10;
        int suppression = 2;
        String status = "Allocated";

        when(policeResourceDAOMock.savePoliceAllocation(disasterId, fighters, supporters, suppression, status)).thenReturn(true);

        Boolean result = policeResourceService.allocatePoliceResources(disasterId, fighters, supporters, suppression, status);
        assertTrue(result);
    }

    @Test
    void testAllocatePoliceResourcesFailure() throws SQLException {
        int disasterId = 1;
        int fighters = 5;
        int supporters = 10;
        int suppression = 2;
        String status = "Failed";

        when(policeResourceDAOMock.savePoliceAllocation(disasterId, fighters, supporters, suppression, status)).thenReturn(false);

        Boolean result = policeResourceService.allocatePoliceResources(disasterId, fighters, supporters, suppression, status);
        assertFalse(result);
    }

    @Test
    void testAllocatePoliceResourcesThrowsSQLException() throws SQLException {
        int disasterId = 1;
        int fighters = 5;
        int supporters = 10;
        int suppression = 2;
        String status = "Error";

        when(policeResourceDAOMock.savePoliceAllocation(disasterId, fighters, supporters, suppression, status))
                .thenThrow(new SQLException("Database error"));

        SQLException thrown = assertThrows(SQLException.class, () -> {
            policeResourceService.allocatePoliceResources(disasterId, fighters, supporters, suppression, status);
        });

        assertEquals("Database error", thrown.getMessage());
    }

    @Test
    void testGetPoliceResourcesSuccessful() throws SQLException {
        int disasterId = 1;
        PoliceResource expectedResource = new PoliceResource(); // Initialize with specific values as necessary

        when(policeResourceDAOMock.getPoliceResources(disasterId)).thenReturn(expectedResource);

        PoliceResource result = policeResourceService.getPoliceResources(disasterId);
        assertEquals(expectedResource, result);
    }

    @Test
    void testGetPoliceResourcesThrowsSQLException() throws SQLException {
        int disasterId = 1;

        when(policeResourceDAOMock.getPoliceResources(disasterId)).thenThrow(new SQLException("Database error"));

        SQLException thrown = assertThrows(SQLException.class, () -> {
            policeResourceService.getPoliceResources(disasterId);
        });

        assertEquals("Database error", thrown.getMessage());
    }

    @Test
    void testUpdatePoliceResourceStatusSuccessful() throws SQLException {
        int disasterId = 1;
        String status = "Updated";

        when(policeResourceDAOMock.savePoliceResourceStatus(disasterId, status)).thenReturn(true);

        Boolean result = policeResourceService.updatePoliceResourceStatus(disasterId, status);
        assertTrue(result);
    }

    @Test
    void testUpdatePoliceResourceStatusFailure() throws SQLException {
        int disasterId = 1;
        String status = "Failed";

        when(policeResourceDAOMock.savePoliceResourceStatus(disasterId, status)).thenReturn(false);

        Boolean result = policeResourceService.updatePoliceResourceStatus(disasterId, status);
        assertFalse(result);
    }

    @Test
    void testUpdatePoliceResourceStatusThrowsSQLException() throws SQLException {
        int disasterId = 1;
        String status = "Error";

        when(policeResourceDAOMock.savePoliceResourceStatus(disasterId, status))
                .thenThrow(new SQLException("Database error"));

        SQLException thrown = assertThrows(SQLException.class, () -> {
            policeResourceService.updatePoliceResourceStatus(disasterId, status);
        });

        assertEquals("Database error", thrown.getMessage());
    }
}

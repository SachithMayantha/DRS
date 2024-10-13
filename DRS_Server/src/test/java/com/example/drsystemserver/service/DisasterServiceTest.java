package com.example.drsystemserver.service;

import com.example.drsystem.model.Disaster;
import com.example.drsystemserver.model.dao.DisasterDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DisasterServiceTest {

    @Mock
    private DisasterDAO disasterDAOMock;

    private DisasterService disasterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        disasterService = new DisasterService(disasterDAOMock);
    }

    @Test
    void testSaveDisasterSuccessful() throws SQLException {
        String type = "Flood";
        String location = "City A";
        String locationType = "Urban";
        String description = "Severe flooding";
        String severity = "High";
        Date date = Date.valueOf("2023-01-01");
        int reportedBy = 1;
        int priorityNo = 5;
        byte[] image = new byte[]{1, 2, 3};

        when(disasterDAOMock.saveDisasterToDatabase(type, location, locationType, description, severity,
                date, reportedBy, priorityNo, image)).thenReturn(true);

        boolean result = disasterService.saveDisaster(type, location, locationType, description, severity,
                date, reportedBy, priorityNo, image);
        assertTrue(result);
    }

    @Test
    void testSaveDisasterFailure() throws SQLException {
        String type = "Flood";
        String location = "City B";
        String locationType = "Rural";
        String description = "Minor flooding";
        String severity = "Low";
        Date date = Date.valueOf("2023-01-02");
        int reportedBy = 2;
        int priorityNo = 1;
        byte[] image = new byte[]{};

        when(disasterDAOMock.saveDisasterToDatabase(type, location, locationType, description, severity,
                date, reportedBy, priorityNo, image)).thenReturn(false);

        boolean result = disasterService.saveDisaster(type, location, locationType, description, severity, date, reportedBy, priorityNo, image);
        assertFalse(result);
    }

    @Test
    void testGetAllDisasters() throws SQLException {
        List<Disaster> expectedDisasters = Arrays.asList(new Disaster(), new Disaster());
        when(disasterDAOMock.getAllDisasters()).thenReturn(expectedDisasters);

        List<Disaster> result = disasterService.getAllDisasters();
        assertEquals(expectedDisasters, result);
    }

    @Test
    void testGetUserDisasters() throws SQLException {
        int userId = 1;
        List<Disaster> expectedDisasters = Arrays.asList(new Disaster());
        when(disasterDAOMock.getUserDisasters(userId)).thenReturn(expectedDisasters);

        List<Disaster> result = disasterService.getUserDisasters(userId);
        assertEquals(expectedDisasters, result);
    }

    @Test
    void testGetDepartmentDisasters() throws SQLException {
        String departmentType = "Health";
        List<Disaster> expectedDisasters = Arrays.asList(new Disaster());
        when(disasterDAOMock.getDepartmentDisasters(departmentType)).thenReturn(expectedDisasters);

        List<Disaster> result = disasterService.getDepartmentDisasters(departmentType);
        assertEquals(expectedDisasters, result);
    }

    @Test
    void testSaveDisasterThrowsSQLException() throws SQLException {
        String type = "Flood";
        String location = "City A";
        Date date = Date.valueOf("2023-01-01");
        byte[] image = new byte[]{1, 2, 3};

        when(disasterDAOMock.saveDisasterToDatabase(anyString(), anyString(), anyString(), anyString(), anyString(),
                any(Date.class), anyInt(), anyInt(), any(byte[].class))).thenThrow(new SQLException("Database error"));

        SQLException thrown = assertThrows(SQLException.class, () -> {
            disasterService.saveDisaster(type, location, "Urban", "Severe flooding", "High", date, 1, 5, image);
        });

        assertEquals("Database error", thrown.getMessage());
    }
}

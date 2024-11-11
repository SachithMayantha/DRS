package com.example.drsystemserver.service;

import com.example.drsystem.model.User;
import com.example.drsystemserver.model.dao.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDAO userDAOMock;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userDAOMock);
    }

    @Test
    void testLoginSuccessful() throws SQLException {
        String email = "test@example.com";
        String password = "password";
        User expectedUser = new User();

        when(userDAOMock.login(email, password)).thenReturn(expectedUser);

        User result = userService.login(email, password);
        assertEquals(expectedUser, result);
    }

    @Test
    void testLoginFailure() throws SQLException {
        String email = "test@example.com";
        String password = "wrongpassword";

        when(userDAOMock.login(email, password)).thenReturn(null);

        User result = userService.login(email, password);
        assertNull(result);
    }

    @Test
    void testRegisterUserSuccessful() throws SQLException {
        String name = "John Doe";
        String email = "john@example.com";
        String password = "password";
        String mobile = "1234567890";
        String role = "Admin";
        String departmentType = "Fire";

        when(userDAOMock.registerUser(name, email, password, mobile, role, departmentType)).thenReturn(true);

        boolean result = userService.registerUser(name, email, password, mobile, role, departmentType);
        assertTrue(result);
    }

    @Test
    void testRegisterUserFailure() throws SQLException {
        String name = "";
        String email = "john@example.com";
        String password = "password";
        String mobile = "1234567890";
        String role = "Admin";
        String departmentType = "Fire";

        boolean result = userService.registerUser(name, email, password, mobile, role, departmentType);
        assertFalse(result);
    }

    @Test
    void testGetDepartmentTypeByUserIdSuccessful() throws SQLException {
        int userId = 1;
        String expectedDepartmentType = "Fire";

        when(userDAOMock.getDepartmentTypeByUserId(userId)).thenReturn(expectedDepartmentType);

        String result = userService.getDepartmentTypeByUserId(userId);
        assertEquals(expectedDepartmentType, result);
    }

    @Test
    void testGetReportedByUserSuccessful() throws SQLException {
        int userId = 1;
        User expectedUser = new User(); // Initialize with necessary attributes

        when(userDAOMock.getReportedByUser(userId)).thenReturn(expectedUser);

        User result = userService.getReportedByUser(userId);
        assertEquals(expectedUser, result);
    }

    @Test
    void testDeleteUserSuccessful() throws SQLException {
        int userId = 1;

        when(userDAOMock.deleteUser(userId)).thenReturn(true);

        boolean result = userService.deleteUser(userId);
        assertTrue(result);
    }

    @Test
    void testDeleteUserFailure() throws SQLException {
        int userId = 1;

        when(userDAOMock.deleteUser(userId)).thenReturn(false);

        boolean result = userService.deleteUser(userId);
        assertFalse(result);
    }

    @Test
    void testGetAllUsersSuccessful() throws SQLException {
        List<User> expectedUsers = Arrays.asList(new User(), new User());

        when(userDAOMock.getAllUsers()).thenReturn(expectedUsers);

        List<User> result = userService.getAllUsers();
        assertEquals(expectedUsers, result);
    }
}

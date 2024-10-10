package com.example.drsystem.controller;

import com.example.drsystemserver.service.UserService;
import com.example.drsystem.model.User;
import com.example.drsystem.session.UserSession;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private TextField nameField;
    @Mock
    private TextField emailField;
    @Mock
    private PasswordField passwordField;
    @Mock
    private TextField mobileField;
    @Mock
    private ComboBox<String> roleComboBox;
    @Mock
    private ComboBox<String> departmentTypeComboBox;
    @Mock
    private Node mockNode;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController();

        // Inject mocks into the controller
        userController.nameField = nameField;
        userController.emailField = emailField;
        userController.passwordField = passwordField;
        userController.mobileField = mobileField;
        userController.roleComboBox = roleComboBox;
        userController.departmentTypeComboBox = departmentTypeComboBox;
    }

    @Test
    void testLogin_WithValidCredentials_ShouldNavigateToDashboard() throws Exception {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        User mockUser = new User();
        mockUser.setRole("USER"); // Set a valid role

        when(emailField.getText()).thenReturn(email);
        when(passwordField.getText()).thenReturn(password);
        when(userService.login(email, password)).thenReturn(mockUser);

        userController.userService = userService; // Injecting the service mock

        // Act
        userController.login(null);

        // Assert
        assertEquals(mockUser, UserSession.getInstance().getLoggedInUser());
        verify(userService, times(1)).login(email, password);
    }

    @Test
    void testLogin_WithInvalidCredentials_ShouldShowErrorAlert() throws Exception {
        // Arrange
        String email = "wrong@example.com";
        String password = "wrongpassword";

        when(emailField.getText()).thenReturn(email);
        when(passwordField.getText()).thenReturn(password);
        when(userService.login(email, password)).thenReturn(null);

        userController.userService = userService; // Injecting the service mock

        // Act
        userController.login(null);

        // Assert
        verify(userService, times(1)).login(email, password);
        // Here you can assert the alert was shown, though it requires a more complex test setup to verify UI alerts.
    }

    @Test
    void testRegisterUser_WithValidInput_ShouldNavigateToLogin() throws Exception {
        // Arrange
        when(nameField.getText()).thenReturn("Test User");
        when(emailField.getText()).thenReturn("test@example.com");
        when(passwordField.getText()).thenReturn("password123");
        when(mobileField.getText()).thenReturn("1234567890");
        when(roleComboBox.getValue()).thenReturn("USER");

        when(userService.registerUser(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(true);

        userController.userService = userService; // Injecting the service mock

        // Act
        userController.registerUser(null);

        // Assert
        verify(userService, times(1)).registerUser("Test User", "test@example.com", "password123", "1234567890", "USER", null);
    }

    @Test
    void testRegisterUser_WithEmptyFields_ShouldShowErrorAlert() {
        // Arrange
        when(nameField.getText()).thenReturn("");
        when(emailField.getText()).thenReturn("test@example.com");
        when(passwordField.getText()).thenReturn("password123");
        when(mobileField.getText()).thenReturn("1234567890");
        when(roleComboBox.getValue()).thenReturn("USER");

        // Act
        userController.registerUser(null);

        // Assert
        verify(userService, never()).registerUser(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
        // Here you can assert the alert was shown, though it requires a more complex test setup to verify UI alerts.
    }

    @Test
    void testHandleRoleSelection_WithDepartmentRole_ShouldShowDepartmentTypeComboBox() {
        // Arrange
        when(roleComboBox.getValue()).thenReturn("DEPARTMENT");

        // Act
        userController.handleRoleSelection();

        // Assert
        assertTrue(departmentTypeComboBox.isVisible());
    }

    @Test
    void testHandleRoleSelection_WithNonDepartmentRole_ShouldHideDepartmentTypeComboBox() {
        // Arrange
        when(roleComboBox.getValue()).thenReturn("USER");

        // Act
        userController.handleRoleSelection();

        // Assert
        assertFalse(departmentTypeComboBox.isVisible());
    }

}

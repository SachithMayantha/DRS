package com.example.drsystemserver.util;

import com.example.drsystem.model.Disaster;
import com.example.drsystem.model.FireResource;
import com.example.drsystem.model.HealthResource;
import com.example.drsystem.model.PoliceResource;
import com.example.drsystem.model.User;
import com.example.drsystemserver.service.DisasterService;
import com.example.drsystemserver.service.FireResourceService;
import com.example.drsystemserver.service.HealthResourceService;
import com.example.drsystemserver.service.PoliceResourceService;
import com.example.drsystemserver.service.UserService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

/**
 * @author sachi
 * handling client request between client and server in multithreaded environment
 */
public class ClientHandler implements Runnable {

    private static final String LOGIN = "LOGIN";
    private static final String REGISTER = "REGISTER";
    private static final String GET_DEPARTMENT_TYPE = "GET_DEPARTMENT_TYPE";
    private static final String GET_ALL_USERS = "GET_ALL_USERS";
    private static final String UPDATE_USER = "UPDATE_USER";
    private static final String DELETE_USER = "DELETE_USER";
    private static final String ALLOCATE_POLICE_RESOURCES = "ALLOCATE_POLICE_RESOURCES";
    private static final String ALLOCATE_HEALTH_RESOURCES = "ALLOCATE_HEALTH_RESOURCES";
    private static final String ALLOCATE_FIRE_RESOURCES = "ALLOCATE_FIRE_RESOURCES";
    private static final String GET_HEALTH_RESOURCES = "GET_HEALTH_RESOURCES";
    private static final String GET_POLICE_RESOURCES = "GET_POLICE_RESOURCES";
    private static final String GET_FIRE_RESOURCES = "GET_FIRE_RESOURCES";
    private static final String UPDATE_HEALTH_RESOURCES = "UPDATE_HEALTH_RESOURCES";
    private static final String UPDATE_POLICE_RESOURCES = "UPDATE_POLICE_RESOURCES";
    private static final String UPDATE_FIRE_RESOURCES = "UPDATE_FIRE_RESOURCES";
    private static final String SAVE_DISASTER = "SAVE_DISASTER";
    private static final String GET_ALL_DISASTERS = "GET_ALL_DISASTERS";
    private static final String GET_USER_DISASTERS = "GET_USER_DISASTERS";
    private static final String GET_DEP_DISASTERS = "GET_DEP_DISASTERS";

    private final Socket clientSocket;
    private final UserService userService;
    private final DisasterService disasterService;
    private final PoliceResourceService policeResourceService;
    private final HealthResourceService healthResourceService;
    private final FireResourceService fireResourceService;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.userService = new UserService();
        this.disasterService = new DisasterService();
        this.policeResourceService = new PoliceResourceService();
        this.healthResourceService = new HealthResourceService();
        this.fireResourceService = new FireResourceService();
    }

    @Override
    public void run() {
        try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())) {

            String requestType = (String) input.readObject();
            handleRequest(requestType, input, output);

        } catch (IOException | ClassNotFoundException | SQLException e) {
            System.err.println("Error in ClientHandler: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeSocket();
        }
    }

    private void handleRequest(String requestType, ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, ClassNotFoundException, SQLException {
        if (requestType == null) {
            sendResponse(output, "Invalid request type");
            return;
        }

        switch (requestType) {
            case LOGIN -> handleLogin(input, output);
            case REGISTER -> handleRegister(input, output);
            case GET_DEPARTMENT_TYPE -> handleGetDepartmentType(input, output);
            case GET_ALL_USERS -> handleGetAllUsers(output);
            case UPDATE_USER -> handleUpdateUser(input, output);
            case DELETE_USER -> handleDeleteUser(input, output);
            case ALLOCATE_POLICE_RESOURCES -> handlePoliceAllocation(input, output);
            case ALLOCATE_HEALTH_RESOURCES -> handleHealthAllocation(input, output);
            case ALLOCATE_FIRE_RESOURCES -> handleFireAllocation(input, output);
            case GET_HEALTH_RESOURCES -> handleGetHealthResources(input, output);
            case GET_POLICE_RESOURCES -> handleGetPoliceResources(input, output);
            case GET_FIRE_RESOURCES -> handleGetFireResources(input, output);
            case UPDATE_HEALTH_RESOURCES -> handleUpdateHealthResources(input, output);
            case UPDATE_POLICE_RESOURCES -> handleUpdatePoliceResources(input, output);
            case UPDATE_FIRE_RESOURCES -> handleUpdateFireResources(input, output);
            case SAVE_DISASTER -> handleSaveDisaster(input, output);
            case GET_ALL_DISASTERS -> handleGetAllDisasters(output);
            case GET_USER_DISASTERS -> handleGetUserDisasters(input, output);
            case GET_DEP_DISASTERS -> handleGetDepartmentSpecificDisasters(input, output);
            default -> sendResponse(output, "Unknown request type");
        }
    }

    private void sendResponse(ObjectOutputStream output, Object response) throws IOException {
        output.writeObject(response);
        output.flush();
    }

    private void closeSocket() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing client socket: " + e.getMessage());
        }
    }

    private void handleLogin(ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, ClassNotFoundException, SQLException {
        String email = (String) input.readObject();
        String password = (String) input.readObject();

        User user = userService.login(email, password);
        sendResponse(output, user);
    }

    private void handleRegister(ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, ClassNotFoundException, SQLException {
        String name = (String) input.readObject();
        String email = (String) input.readObject();
        String password = (String) input.readObject();
        String mobile = (String) input.readObject();
        String role = (String) input.readObject();
        String departmentType = (String) input.readObject();

        boolean isRegistered = userService.registerUser(name, email, password, mobile, role, departmentType);
        sendResponse(output, isRegistered);
    }

    private void handleGetDepartmentType(ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, ClassNotFoundException, SQLException {
        int userId = (int) input.readObject();
        String departmentType = userService.getDepartmentTypeByUserId(userId);
        sendResponse(output, departmentType);
    }

    private void handleGetAllUsers(ObjectOutputStream output) throws IOException {
        try {
            List<User> users = userService.getAllUsers();
            sendResponse(output, users);
        } catch (SQLException e) {
            sendResponse(output, null);
            System.err.println("Error fetching users: " + e.getMessage());
        }
    }

    private void handleUpdateUser(ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, ClassNotFoundException, SQLException {
        User updatedUser = (User) input.readObject();
        boolean isUpdated = userService.updateUser(updatedUser);
        sendResponse(output, isUpdated);
    }

    private void handleDeleteUser(ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, SQLException, ClassNotFoundException {
        int userId = (int) input.readObject();
        boolean isDeleted = userService.deleteUser(userId);
        sendResponse(output, isDeleted);
    }

    private void handlePoliceAllocation(ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, ClassNotFoundException, SQLException {
        int disasterId = (int) input.readObject();
        int policemanCount = (int) input.readObject();
        int controllerCount = (int) input.readObject();
        int investigatorCount = (int) input.readObject();
        String status = (String) input.readObject();

        boolean isAllocated = policeResourceService.allocatePoliceResources(disasterId, policemanCount, controllerCount, investigatorCount, status);
        sendResponse(output, isAllocated);
    }

    private void handleHealthAllocation(ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, ClassNotFoundException, SQLException {
        int disasterId = (int) input.readObject();
        int doctors = (int) input.readObject();
        int nurses = (int) input.readObject();
        int ambulance = (int) input.readObject();
        String status = (String) input.readObject();

        boolean isAllocated = healthResourceService.allocateHealthResources(disasterId, doctors, nurses, ambulance, status);
        sendResponse(output, isAllocated);
    }

    private void handleFireAllocation(ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, ClassNotFoundException, SQLException {
        int disasterId = (int) input.readObject();
        int fighters = (int) input.readObject();
        int supporters = (int) input.readObject();
        int suppression = (int) input.readObject();
        String status = (String) input.readObject();

        boolean isAllocated = fireResourceService.allocateFireResources(disasterId, fighters, supporters, suppression, status);
        sendResponse(output, isAllocated);
    }

    private void handleGetHealthResources(ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, ClassNotFoundException, SQLException {
        int disasterId = (int) input.readObject();
        HealthResource healthResources = healthResourceService.getHealthResources(disasterId);
        sendResponse(output, healthResources);
    }

    private void handleGetPoliceResources(ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, ClassNotFoundException, SQLException {
        int disasterId = (int) input.readObject();
        PoliceResource policeResources = policeResourceService.getPoliceResources(disasterId);
        sendResponse(output, policeResources);
    }

    private void handleGetFireResources(ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, ClassNotFoundException, SQLException {
        int disasterId = (int) input.readObject();
        FireResource fireResources = fireResourceService.getFireResources(disasterId);
        sendResponse(output, fireResources);
    }

    private void handleUpdateHealthResources(ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, ClassNotFoundException, SQLException {
        int disasterId = (int) input.readObject();
        String status = (String) input.readObject();
        Boolean isUpdated = healthResourceService.updateHealthResourceStatus(disasterId,status);
        sendResponse(output, isUpdated);
    }

    private void handleUpdatePoliceResources(ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, ClassNotFoundException, SQLException {
        int disasterId = (int) input.readObject();
        String status = (String) input.readObject();
        Boolean isUpdated = policeResourceService.updatePoliceResourceStatus(disasterId,status);
        sendResponse(output, isUpdated);
    }

    private void handleUpdateFireResources(ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, ClassNotFoundException, SQLException {
        int disasterId = (int) input.readObject();
        String status = (String) input.readObject();
        Boolean isUpdated = fireResourceService.updateFireResourceStatus(disasterId,status);
        sendResponse(output, isUpdated);
    }

    private void handleSaveDisaster(ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, ClassNotFoundException, SQLException {
        // Read data from the client
        String type = (String) input.readObject();
        String location = (String) input.readObject();
        String locationType = (String) input.readObject();
        String description = (String) input.readObject();
        String severity = (String) input.readObject();
        java.sql.Date date = (java.sql.Date) input.readObject();
        int reportedBy = (int) input.readObject();
        int priorityNo = (int) input.readObject();
        byte[] imageBytes = (byte[]) input.readObject(); // If the image is sent as a byte array

        // Process and save disaster report
        boolean isSaved = disasterService.saveDisaster(type, location, locationType, description, severity, date, reportedBy, priorityNo, imageBytes);
        sendResponse(output, isSaved);
    }

    private void handleGetAllDisasters(ObjectOutputStream output) throws IOException {
        try {
            List<Disaster> disasters = disasterService.getAllDisasters();
            sendResponse(output, disasters);
        } catch (SQLException e) {
            sendResponse(output, null);
            System.err.println("Error fetching disasters: " + e.getMessage());
        }
    }

    private void handleGetUserDisasters(ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, ClassNotFoundException, SQLException {
        int userId = (int) input.readObject();
        List<Disaster> userDisasters = disasterService.getUserDisasters(userId);
        sendResponse(output, userDisasters);
    }

    private void handleGetDepartmentSpecificDisasters(ObjectInputStream input, ObjectOutputStream output) 
            throws IOException, ClassNotFoundException, SQLException {
        String departmentType = (String) input.readObject();
        List<Disaster> departmentDisasters = disasterService.getDepartmentDisasters(departmentType);
        sendResponse(output, departmentDisasters);
    }
}

package com.example.drsystemserver.service;

import com.example.drsystem.model.User;
import com.example.drsystemserver.model.dao.UserDAO;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jayath
 * User Service for User login, logout and CRUD operations
 */
public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User login(String email, String password) throws SQLException {
        return userDAO.login(email, password);
    }

    public boolean registerUser(String name, String email, String password, String mobile, String role, String departmentType) throws SQLException {
        if (name == null || name.isEmpty() || email == null || email.isEmpty() ||
                password == null || password.isEmpty() || mobile == null || mobile.isEmpty() || role == null || role.isEmpty()) {
            return false;
        }
        return userDAO.registerUser(name, email, password, mobile, role, departmentType);
    }

    public String getDepartmentTypeByUserId(int userId) throws SQLException {
        return userDAO.getDepartmentTypeByUserId(userId);
    }

    public User getReportedByUser(int userId) throws SQLException {
        return userDAO.getReportedByUser(userId);
    }

    public boolean deleteUser(int userId) throws SQLException {
        return userDAO.deleteUser(userId);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    public boolean updateUser(User selectedUser) throws SQLException {
        return userDAO.updateUser(selectedUser);
    }
}

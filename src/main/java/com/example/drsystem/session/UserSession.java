package com.example.drsystem.session;

import com.example.drsystem.model.User;

public class UserSession {
    private static UserSession instance;
    private User loggedInUser;
    private static String loggedUserDepartment;


    private UserSession() {
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedUserDepartment(String department) {
        loggedUserDepartment = department;
    }

    public static String getLoggedUserDepartment() {
        return loggedUserDepartment;
    }

    public void clearSession() {
        loggedInUser = null;
        loggedUserDepartment = null;
    }
}

package com.example.drsystem.model;

public class Department {

    private int departmentId;
    private String name;
    private String email;
    private String mobile;

    public Department(int departmentId, String name, String email, String mobile) {

        this.departmentId = departmentId;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

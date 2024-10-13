package com.example.drsystem.model;

public class HealthResource {
    private int healthId;

    private int disasterId;

    private int doctors;

    private int nurses;

    private int ambulances;

    private String status;

    public HealthResource() {
    }

    
    public HealthResource(int healthId, int disasterId, int doctors, int nurses, int ambulances, String status) {
        this.healthId = healthId;
        this.disasterId = disasterId;
        this.doctors = doctors;
        this.nurses = nurses;
        this.ambulances = ambulances;
        this.status = status;
    }

    public int getHealthId() {
        return healthId;
    }

    public void setHealthId(int healthId) {
        this.healthId = healthId;
    }

    public int getDisasterId() {
        return disasterId;
    }

    public void setDisasterId(int disasterId) {
        this.disasterId = disasterId;
    }

    public int getDoctors() {
        return doctors;
    }

    public void setDoctors(int doctors) {
        this.doctors = doctors;
    }

    public int getNurses() {
        return nurses;
    }

    public void setNurses(int nurses) {
        this.nurses = nurses;
    }

    public int getAmbulances() {
        return ambulances;
    }

    public void setAmbulances(int ambulances) {
        this.ambulances = ambulances;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

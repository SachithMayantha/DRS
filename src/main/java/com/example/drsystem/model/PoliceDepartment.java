package com.example.drsystem.model;

public class PoliceDepartment {

    private int policeId;

    private int disasterId;

    private int policeman;

    private int trafficControllers;

    private int investigators;

    private String status;

    public PoliceDepartment(int policeId, int disasterId, int policeman, int trafficControllers, int investigators, String status) {
        this.policeId = policeId;
        this.disasterId = disasterId;
        this.policeman = policeman;
        this.trafficControllers = trafficControllers;
        this.investigators = investigators;
        this.status = status;
    }

    public int getPoliceId() {
        return policeId;
    }

    public void setPoliceId(int policeId) {
        this.policeId = policeId;
    }

    public int getDisasterId() {
        return disasterId;
    }

    public void setDisasterId(int disasterId) {
        this.disasterId = disasterId;
    }

    public int getPoliceman() {
        return policeman;
    }

    public void setPoliceman(int policeman) {
        this.policeman = policeman;
    }

    public int getTrafficControllers() {
        return trafficControllers;
    }

    public void setTrafficControllers(int trafficControllers) {
        this.trafficControllers = trafficControllers;
    }

    public int getInvestigators() {
        return investigators;
    }

    public void setInvestigators(int investigators) {
        this.investigators = investigators;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

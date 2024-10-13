package com.example.drsystem.model;

public class FireEmergency {

    private int fireId;

    private int disasterId;

    private int fighters;

    private int supporters;

    private int suppression;

    private String status;

    public FireEmergency(int fireId, int disasterId, int fighters, int supporters, int suppression, String status) {
        this.fireId = fireId;
        this.disasterId = disasterId;
        this.fighters = fighters;
        this.supporters = supporters;
        this.suppression = suppression;
        this.status = status;
    }

    public int getFireId() {
        return fireId;
    }

    public void setFireId(int fireId) {
        this.fireId = fireId;
    }

    public int getDisasterId() {
        return disasterId;
    }

    public void setDisasterId(int disasterId) {
        this.disasterId = disasterId;
    }

    public int getFighters() {
        return fighters;
    }

    public void setFighters(int fighters) {
        this.fighters = fighters;
    }

    public int getSupporters() {
        return supporters;
    }

    public void setSupporters(int supporters) {
        this.supporters = supporters;
    }

    public int getSuppression() {
        return suppression;
    }

    public void setSuppression(int suppression) {
        this.suppression = suppression;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

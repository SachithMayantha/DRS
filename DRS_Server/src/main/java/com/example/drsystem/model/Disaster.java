package com.example.drsystem.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author sanduni
 * Model for disaster
 */
public class Disaster implements Serializable {

    private static final long serialVersionUID = 1L;
    private int disasterId;
    private String type;
    private String location;
    private String locationType;
    private String description;
    private String severity;
    private LocalDate date;
    private User reportedBy;
    private int priorityNo;
    private byte[] image;

    public Disaster(int disasterId, String type, String location, String locationType, String description, String severity, LocalDate date, User reportedBy, int priorityNo, byte[] image) {
        this.disasterId = disasterId;
        this.type = type;
        this.location = location;
        this.locationType = locationType;
        this.description = description;
        this.severity = severity;
        this.date = date;
        this.reportedBy = reportedBy;
        this.priorityNo = priorityNo;
        this.image = image;
    }

    public Disaster() {
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getPriorityNo() {
        return priorityNo;
    }

    public void setPriorityNo(int priorityNo) {
        this.priorityNo = priorityNo;
    }

    public int getDisasterId() {
        return disasterId;
    }

    public void setDisasterId(int disasterId) {
        this.disasterId = disasterId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(User reportedBy) {
        this.reportedBy = reportedBy;
    }
}

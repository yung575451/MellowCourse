package com.hungphamcom.mellowcourse.model;

public class U_Status {
    private String userId;
    private String status;

    public U_Status(String userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    public U_Status() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

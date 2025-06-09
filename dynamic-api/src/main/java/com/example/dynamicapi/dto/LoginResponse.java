package com.example.dynamicapi.dto;

public class LoginResponse {
    private boolean success;
    private String username;

    public LoginResponse() {
    }

    public LoginResponse(boolean success, String username) {
        this.success = success;
        this.username = username;
    }

    // Getters y setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

package com.example.dynamicapi.dto;

public class LoginRequest {
    private String username;
    private String password;

    // Constructor por defecto (necesario para @RequestBody)
    public LoginRequest() {
    }

    // Getters y setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        System.out.println("Username set to: " + username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

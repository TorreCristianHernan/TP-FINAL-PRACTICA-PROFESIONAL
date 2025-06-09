package com.example.dynamicapi.dto;

public class LoginResponseDTO {
    private boolean success;
    private String username; // opcional, puede quedar nulo si !success

    public LoginResponseDTO(boolean success, String username) {
        this.success  = success;
        this.username = username;
    }
    // getters
}

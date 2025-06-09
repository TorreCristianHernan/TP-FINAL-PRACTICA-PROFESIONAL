package com.example.dynamicapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dynamicapi.dto.AuthRequest;
import com.example.dynamicapi.dto.AuthResponse;
import com.example.dynamicapi.security.JwtUtil;
import com.example.dynamicapi.service.interfaces.IUserService;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private IUserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return userService.login(request.getUsername(), request.getPassword())
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getUsername());
                    return ResponseEntity.ok(new AuthResponse(token));
                })
                .orElseGet(() -> ResponseEntity.status(401).body(new AuthResponse("Credenciales inválidas")));
    }
}

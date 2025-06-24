package com.example.dynamicapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dynamicapi.model.Client;
import com.example.dynamicapi.model.User;
import com.example.dynamicapi.security.JwtUtil;
import com.example.dynamicapi.service.ClientService;
import com.example.dynamicapi.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    private User extractUser(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header != null && header.startsWith("Bearer ") ? header.substring(7) : null;
        String username = jwtUtil.extractUsername(token);
        return userService.getUserByUsername(username).orElseThrow();
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAll(HttpServletRequest request) {
        User user = extractUser(request);
        return ResponseEntity.ok(clientService.getAll(user));
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> count(HttpServletRequest request) {
        User user = extractUser(request);
        return ResponseEntity.ok(clientService.count(user));
    }
}

package com.ferrefull.controller;

import com.ferrefull.controller.response.LoginResponse;
import com.ferrefull.entity.User;
import com.ferrefull.entity.request.AuthRequest;
import com.ferrefull.service.impl.JwtService;
import com.ferrefull.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public LoginResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            Optional<User> user = userService.getUserByEmail(authRequest.getEmail());
            if (user.isPresent()){
                return LoginResponse.builder().token(jwtService.generateToken(user.get())).build();
            }
        }
        throw new UsernameNotFoundException("invalid user request !");
    }
}

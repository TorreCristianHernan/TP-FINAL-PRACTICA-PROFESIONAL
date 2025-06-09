package com.example.dynamicapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dynamicapi.model.User;
import com.example.dynamicapi.repository.UserRepository; // para comparar hashes

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean validate(String username, String rawPassword) {
        return userRepository.findByUsername(username)
          .map(User::getPassword)                       // obtén el hash almacenado
          .filter(hashedPwd -> 
              passwordEncoder.matches(rawPassword, hashedPwd)
          )
          .isPresent();
    }
}

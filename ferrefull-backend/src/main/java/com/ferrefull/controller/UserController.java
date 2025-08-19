package com.ferrefull.controller;

import com.ferrefull.entity.request.AuthRequest;
import com.ferrefull.entity.User;
import com.ferrefull.service.impl.JwtService;
import com.ferrefull.service.impl.UserServiceImpl;
import com.ferrefull.util.Util;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<User>> listUsers(){
        return ResponseEntity.ok(userService.listUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id){
        return userService.getUser(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<User> addNewUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, Util.formatMessage(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @Valid @RequestBody User user, BindingResult result){
        if (result.hasErrors()){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, Util.formatMessage(result));
        }
        user.setId(id);
        User userDB = userService.updateUser(user);
        if (Objects.isNull(userDB)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDB);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id){
        User userDB = userService.deleteUser(id);
        if (Objects.isNull(userDB)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDB);
    }
}

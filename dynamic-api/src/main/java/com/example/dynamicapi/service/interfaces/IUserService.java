package com.example.dynamicapi.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.example.dynamicapi.model.User;

public interface IUserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);

    Optional<User> login(String username, String password);
}

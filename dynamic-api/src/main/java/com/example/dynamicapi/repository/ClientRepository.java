package com.example.dynamicapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dynamicapi.model.Client;
import com.example.dynamicapi.model.User;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    List<Client> findByAssignedTo(User user);
}

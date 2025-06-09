package com.example.dynamicapi.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.example.dynamicapi.model.Client;
import com.example.dynamicapi.model.User;

public interface IClientService {
    List<Client> getAll(User requester);
    Optional<Client> getById(Integer id, User requester);
    Client create(Client client, User requester);
    Client update(Integer id, Client updatedClient, User requester);
    void delete(Integer id, User requester);
    int count(User requester);
}
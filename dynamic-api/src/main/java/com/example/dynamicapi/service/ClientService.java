package com.example.dynamicapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dynamicapi.model.Client;
import com.example.dynamicapi.model.User;
import com.example.dynamicapi.repository.ClientRepository;
import com.example.dynamicapi.service.interfaces.IClientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService implements IClientService {

    private final ClientRepository clientRepository;

    @Override
    public List<Client> getAll(User requester) {
        User.Role roleUser = User.Role.admin;

        if (requester.getRole().equals(roleUser)) {
            return clientRepository.findAll();
        }
        return clientRepository.findByAssignedTo(requester);
    }

    @Override
    public Optional<Client> getById(Integer id, User requester) {
        return null;
    }

    @Override
    public Client create(Client client, User requester) {
        return null;
    }

    @Override
    public Client update(Integer id, Client updatedClient, User requester) {
        return null;
    }

    @Override
    public void delete(Integer id, User requester) {
    }

    @Override
    public int count(User requester) {
        User.Role roleUser = User.Role.admin;

        if (requester.getRole().equals(roleUser)) {
            return (int) clientRepository.count();
        }
        return clientRepository.findByAssignedTo(requester).size();
    }
}

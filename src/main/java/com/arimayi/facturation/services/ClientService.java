package com.arimayi.facturation.services;

import org.springframework.stereotype.Service;

import com.arimayi.facturation.entities.Client;
import com.arimayi.facturation.repositories.ClientRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des clients.
 * Gère les opérations CRUD Client.
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    //Récupère la liste complète des clients enregistrés.

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    //Récupère un client spécifique par son identifiant.
     
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    //Crée un nouveau client dans le système.
     
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }
}


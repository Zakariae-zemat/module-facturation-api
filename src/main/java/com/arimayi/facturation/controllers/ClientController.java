package com.arimayi.facturation.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.arimayi.facturation.entities.Client;
import com.arimayi.facturation.services.ClientService;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des clients.
 * Expose les endpoints API pour les opérations sur les clients.
 */
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    //Récupère tous les clients enregistrés.
     
    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    //Récupère un client spécifique par son identifiant.
    
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Crée un nouveau client.
     
    @PostMapping
    public ResponseEntity<Client> createClient(@Valid @RequestBody Client client) {
        // DÉLÉGATION : Le service gère la création et les validations
        Client created = clientService.createClient(client);
        return ResponseEntity.ok(created);
    }
}


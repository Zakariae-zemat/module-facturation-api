package com.arimayi.facturation.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.arimayi.facturation.entities.Client;
import com.arimayi.facturation.repositories.ClientRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    private ClientRepository clientRepository;
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        clientRepository = mock(ClientRepository.class);
        clientService = new ClientService(clientRepository);
    }

    @Test
    void testCreateClient_shouldReturnSavedClient() {
        Client input = Client.builder()
                .nom("Test")
                .email("test@arimayi.com")
                .siret("12345678901234")
                .dateCreation(LocalDate.now())
                .build();

        when(clientRepository.save(any(Client.class))).thenReturn(input);

        Client result = clientService.createClient(input);

        assertNotNull(result);
        assertEquals("Test", result.getNom());
        assertEquals("test@arimayi.com", result.getEmail());
        assertEquals("12345678901234", result.getSiret());
    }
}


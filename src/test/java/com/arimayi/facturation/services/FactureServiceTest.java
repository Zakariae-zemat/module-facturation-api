package com.arimayi.facturation.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.arimayi.facturation.entities.Client;
import com.arimayi.facturation.entities.Facture;
import com.arimayi.facturation.entities.LigneFacture;
import com.arimayi.facturation.entities.TauxTVA;
import com.arimayi.facturation.repositories.ClientRepository;
import com.arimayi.facturation.repositories.FactureRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FactureServiceTest {

    private FactureRepository factureRepository;
    private ClientRepository clientRepository;
    private FactureService factureService;

    @BeforeEach
    void setUp() {
        // Création de mocks pour isoler le service de la BDD réelle
        factureRepository = mock(FactureRepository.class);
        clientRepository = mock(ClientRepository.class);

        // Injection des dépendances dans le service à tester
        factureService = new FactureService(factureRepository, clientRepository);
    }

    @Test
    void testCreateFacture_shouldCalculateTotals() {
        // Création d'un client fictif
        Client mockClient = Client.builder()
                .id(1L)
                .nom("Zemat")
                .email("contact@zemat.com")
                .siret("12345678901234")
                .build();

        // Simulation de la récupération du client depuis le repository
        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));

        // Création de deux lignes de facture avec différents taux de TVA
        LigneFacture ligne1 = LigneFacture.builder()
                .description("Service A")
                .quantite(2)
                .prixUnitaireHT(BigDecimal.valueOf(100))
                .tauxTVA(TauxTVA.TVA_20)
                .build();

        LigneFacture ligne2 = LigneFacture.builder()
                .description("Service B")
                .quantite(1)
                .prixUnitaireHT(BigDecimal.valueOf(200))
                .tauxTVA(TauxTVA.TVA_10)
                .build();

        // Assemblage de la facture avec les lignes
        Facture facture = Facture.builder()
                .client(mockClient)
                .lignes(List.of(ligne1, ligne2))
                .build();

        // Simulation du comportement de save() : retourne ce qu'on lui donne
        when(factureRepository.save(any(Facture.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Exécution : appel du service
        Facture result = factureService.createFacture(facture);

        // Vérification que les totaux sont bien calculés
        assertNotNull(result.getTotalHT());
        assertNotNull(result.getTotalTVA());
        assertNotNull(result.getTotalTTC());

        assertEquals(BigDecimal.valueOf(400.00).setScale(2), result.getTotalHT());   // 200 + 200
        assertEquals(BigDecimal.valueOf(60.00).setScale(2), result.getTotalTVA());   // 40 (20%) + 20 (10%)
        assertEquals(BigDecimal.valueOf(460.00).setScale(2), result.getTotalTTC());  // 400 + 60
    }
}

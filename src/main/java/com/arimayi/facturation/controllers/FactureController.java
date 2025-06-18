package com.arimayi.facturation.controllers;


import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.arimayi.facturation.entities.Facture;
import com.arimayi.facturation.services.FactureService;

import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des factures.
 * Expose les endpoints API pour les opérations sur les factures.
 */
@RestController
@RequestMapping("/factures")
public class FactureController {

    private final FactureService factureService;

    public FactureController(FactureService factureService) {
        this.factureService = factureService;
    }

    //Récupère toutes les factures.
     
    @GetMapping
    public List<Facture> getAllFactures() {
        return factureService.getAllFactures();
    }

    //Récupère une facture par son identifiant.
    
    @GetMapping("/{id}")
    public ResponseEntity<Facture> getFactureById(@PathVariable Long id) {
        return factureService.getFactureById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Crée une nouvelle facture.
     
    @PostMapping
    public ResponseEntity<Facture> createFacture(@Valid @RequestBody Facture facture) {
        try {
            // DÉLÉGATION : Le service gère toute la logique de création
            Facture created = factureService.createFacture(facture);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            // GESTION : Erreur de validation métier (client inexistant, facture invalide)
            return ResponseEntity.badRequest().body(null);
        }
    }

    //Exporte une facture au format JSON structuré.
     
    @GetMapping("/{id}/export")
    public ResponseEntity<?> exportFacture(@PathVariable Long id) {
        return factureService.exportFacture(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Recherche de factures selon différents critères.
     
    @GetMapping("/search")
    public List<Facture> searchFactures(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return factureService.searchFactures(clientId, date);
    }



}

package com.arimayi.facturation.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.arimayi.facturation.entities.TauxTVA;

/**
 * DTO (Data Transfer Object) pour l'export de facture au format JSON.
 * 
 * UTILISATION MÉTIER :
 * - Transformation de l'entité Facture en format d'export
 * - Structure optimisée pour l'API REST
 * - Séparation des données client et lignes pour une meilleure lisibilité
 * - Format standardisé pour l'externalisation des factures
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FactureExportDto {

    /**
     * Identifiant unique de la facture
     */
    private Long factureId;
    
    /**
     * Date de création de la facture
     */
    private LocalDate date;

    /**
     * Informations du client (structure imbriquée)
     */
    private ClientInfo client;

    /**
     * Liste des lignes de facture (structure imbriquée)
     */
    private List<LigneInfo> lignes;

    /**
     * Montant total hors taxes
     */
    private BigDecimal totalHT;
    
    /**
     * Montant total de la TVA
     */
    private BigDecimal totalTVA;
    
    /**
     * Montant total toutes taxes comprises
     */
    private BigDecimal totalTTC;

    /**
     * Classe interne représentant les informations client pour l'export.
     * 
     * STRUCTURE MÉTIER :
     * - Données essentielles du client pour l'export
     * - Format simplifié sans l'ID (non nécessaire pour l'export)
     */
    @Data
    @AllArgsConstructor
    public static class ClientInfo {
        private String nom;
        private String email;
        private String siret;
    }

    /**
     * Classe interne représentant une ligne de facture pour l'export.
     * 
     * STRUCTURE MÉTIER :
     * - Données de la ligne sans l'ID et la référence à la facture
     * - Format optimisé pour la sérialisation JSON
     * - Conservation du taux de TVA pour traçabilité
     */
    @Data
    @AllArgsConstructor
    public static class LigneInfo {
        private String description;
        private int quantite;
        private BigDecimal prixUnitaireHT;
        private TauxTVA tauxTVA;
    }
}

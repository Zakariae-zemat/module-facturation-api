package com.arimayi.facturation.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arimayi.facturation.entities.Facture;

/**
 * Repository pour la gestion des entités Facture.
 * Fournit les méthodes de recherche métier pour les factures.
 */
public interface FactureRepository extends JpaRepository<Facture, Long> {
    
    /**
     * Recherche toutes les factures d'un client spécifique.
     * 
     * UTILISATION MÉTIER :
     * - Historique des factures d'un client
     * - Suivi de la facturation par client
     * 
     * @param clientId Identifiant du client
     * @return Liste des factures du client
     */
    List<Facture> findByClientId(Long clientId);

    /**
     * Recherche toutes les factures créées à une date spécifique.
     * 
     * UTILISATION MÉTIER :
     * - Reporting journalier
     * - Consultation des factures d'une période
     * 
     * @param date Date de création des factures
     * @return Liste des factures de la date
     */
    List<Facture> findByDate(LocalDate date);

    /**
     * Recherche les factures d'un client à une date spécifique.
     * 
     * UTILISATION MÉTIER :
     * - Recherche précise par client et date
     * - Filtrage combiné pour les rapports
     * 
     * @param clientId Identifiant du client
     * @param date Date de création des factures
     * @return Liste des factures correspondant aux critères
     */
    List<Facture> findByClientIdAndDate(Long clientId, LocalDate date);
}
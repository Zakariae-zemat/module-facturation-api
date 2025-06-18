package com.arimayi.facturation.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.arimayi.facturation.dto.FactureExportDto;
import com.arimayi.facturation.entities.Client;
import com.arimayi.facturation.entities.Facture;
import com.arimayi.facturation.entities.LigneFacture;
import com.arimayi.facturation.repositories.ClientRepository;
import com.arimayi.facturation.repositories.FactureRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des factures.
 * Contient toute la logique de calcul et de validation des factures.
 */
@Service
public class FactureService {

    private final FactureRepository factureRepository;
    private final ClientRepository clientRepository;

    public FactureService(FactureRepository factureRepository, ClientRepository clientRepository) {
        this.factureRepository = factureRepository;
        this.clientRepository = clientRepository;
    }

    //Récupère toutes les factures du système
     
    public List<Facture> getAllFactures() {
        return factureRepository.findAll();
    }

    //Récupère une facture par son identifiant
     
    public Optional<Facture> getFactureById(Long id) {
        return factureRepository.findById(id);
    }

    //Crée une nouvelle facture avec calculs automatiques des montants.
    
    @Transactional
    public Facture createFacture(Facture facture) {
        // VALIDATION : Vérification de la présence d'au moins une ligne de facture
        if (facture.getLignes() == null || facture.getLignes().isEmpty()) {
            throw new IllegalArgumentException("Une facture doit contenir au moins une ligne.");
        }

        // VALIDATION : Vérification de l'existence du client en base
        Client client = clientRepository.findById(facture.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable"));

        facture.setClient(client);

        // CALCULS : Initialisation des totaux
        BigDecimal totalHT = BigDecimal.ZERO;
        BigDecimal totalTVA = BigDecimal.ZERO;

        // CALCULS : Parcours de chaque ligne pour calculer les montants
        for (LigneFacture ligne : facture.getLignes()) {
            // Calcul du montant HT de la ligne : prix unitaire × quantité
            BigDecimal ligneHT = ligne.getPrixUnitaireHT().multiply(BigDecimal.valueOf(ligne.getQuantite()));
            
            // Calcul de la TVA de la ligne : montant HT × taux TVA / 100
            // Utilisation de HALF_UP pour l'arrondi (standard comptable)
            BigDecimal ligneTVA = ligneHT.multiply(BigDecimal.valueOf(ligne.getTauxTVA().getTaux()))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            // Accumulation des totaux
            totalHT = totalHT.add(ligneHT);
            totalTVA = totalTVA.add(ligneTVA);

            // Association de la ligne à la facture (relation bidirectionnelle)
            ligne.setFacture(facture);
        }

        // CALCULS MÉTIER : Finalisation des totaux avec arrondi à 2 décimales
        facture.setTotalHT(totalHT.setScale(2, RoundingMode.HALF_UP));
        facture.setTotalTVA(totalTVA.setScale(2, RoundingMode.HALF_UP));
        // Total TTC = Total HT + Total TVA
        facture.setTotalTTC(totalHT.add(totalTVA).setScale(2, RoundingMode.HALF_UP));

        return factureRepository.save(facture);
    }

    //Exporte une facture au format JSON structuré.
    public Optional<FactureExportDto> exportFacture(Long id) {
        return factureRepository.findById(id).map(facture -> {
            var client = facture.getClient();
            
            // TRANSFORMATION : Conversion des lignes de facture en DTO
            var lignes = facture.getLignes().stream().map(ligne -> new FactureExportDto.LigneInfo(
                    ligne.getDescription(),
                    ligne.getQuantite(),
                    ligne.getPrixUnitaireHT(),
                    ligne.getTauxTVA()
            )).toList();

            // CONSTRUCTION : Assemblage du DTO d'export avec toutes les données
            return FactureExportDto.builder()
                    .factureId(facture.getId())
                    .date(facture.getDate())
                    .client(new FactureExportDto.ClientInfo(
                            client.getNom(), client.getEmail(), client.getSiret()
                    ))
                    .lignes(lignes)
                    .totalHT(facture.getTotalHT())
                    .totalTVA(facture.getTotalTVA())
                    .totalTTC(facture.getTotalTTC())
                    .build();
        });
    }

    //Recherche de factures selon différents critères.
    public List<Facture> searchFactures(Long clientId, LocalDate date) {
        // RECHERCHE : Logique conditionnelle selon les critères fournis
        if (clientId != null && date != null) {
            // Recherche par client ET date (critères multiples)
            return factureRepository.findByClientIdAndDate(clientId, date);
        } else if (clientId != null) {
            // Recherche par client uniquement
            return factureRepository.findByClientId(clientId);
        } else if (date != null) {
            // Recherche par date uniquement
            return factureRepository.findByDate(date);
        } else {
            // Aucun critère : retour de toutes les factures
            return factureRepository.findAll();
        }
    }


}

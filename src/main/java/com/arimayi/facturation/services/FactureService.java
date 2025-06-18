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

@Service
public class FactureService {

    private final FactureRepository factureRepository;
    private final ClientRepository clientRepository;

    public FactureService(FactureRepository factureRepository, ClientRepository clientRepository) {
        this.factureRepository = factureRepository;
        this.clientRepository = clientRepository;
    }

    public List<Facture> getAllFactures() {
        return factureRepository.findAll();
    }

    public Optional<Facture> getFactureById(Long id) {
        return factureRepository.findById(id);
    }

    @Transactional
    public Facture createFacture(Facture facture) {
        // Valider présence d'au moins une ligne
        if (facture.getLignes() == null || facture.getLignes().isEmpty()) {
            throw new IllegalArgumentException("Une facture doit contenir au moins une ligne.");
        }

        // Valider que le client existe
        Client client = clientRepository.findById(facture.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable"));

        facture.setClient(client);

        // Calculs
        BigDecimal totalHT = BigDecimal.ZERO;
        BigDecimal totalTVA = BigDecimal.ZERO;

        for (LigneFacture ligne : facture.getLignes()) {
            BigDecimal ligneHT = ligne.getPrixUnitaireHT().multiply(BigDecimal.valueOf(ligne.getQuantite()));
            BigDecimal ligneTVA = ligneHT.multiply(BigDecimal.valueOf(ligne.getTauxTVA().getTaux()))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            totalHT = totalHT.add(ligneHT);
            totalTVA = totalTVA.add(ligneTVA);

            ligne.setFacture(facture);
        }

        facture.setTotalHT(totalHT.setScale(2, RoundingMode.HALF_UP));
        facture.setTotalTVA(totalTVA.setScale(2, RoundingMode.HALF_UP));
        facture.setTotalTTC(totalHT.add(totalTVA).setScale(2, RoundingMode.HALF_UP));

        return factureRepository.save(facture);
    }

    public Optional<FactureExportDto> exportFacture(Long id) {
        return factureRepository.findById(id).map(facture -> {
            var client = facture.getClient();
            var lignes = facture.getLignes().stream().map(ligne -> new FactureExportDto.LigneInfo(
                    ligne.getDescription(),
                    ligne.getQuantite(),
                    ligne.getPrixUnitaireHT(),
                    ligne.getTauxTVA()
            )).toList();

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

    public List<Facture> searchFactures(Long clientId, LocalDate date) {
        if (clientId != null && date != null) {
            return factureRepository.findByClientIdAndDate(clientId, date);
        } else if (clientId != null) {
            return factureRepository.findByClientId(clientId);
        } else if (date != null) {
            return factureRepository.findByDate(date);
        } else {
            return factureRepository.findAll();
        }
    }


}

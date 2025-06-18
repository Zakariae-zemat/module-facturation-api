package com.arimayi.facturation.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.arimayi.facturation.entities.TauxTVA;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FactureExportDto {

    private Long factureId;
    private LocalDate date;

    private ClientInfo client;

    private List<LigneInfo> lignes;

    private BigDecimal totalHT;
    private BigDecimal totalTVA;
    private BigDecimal totalTTC;

    @Data
    @AllArgsConstructor
    public static class ClientInfo {
        private String nom;
        private String email;
        private String siret;
    }

    @Data
    @AllArgsConstructor
    public static class LigneInfo {
        private String description;
        private int quantite;
        private BigDecimal prixUnitaireHT;
        private TauxTVA tauxTVA;
    }
}

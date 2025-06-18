package com.arimayi.facturation.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneFacture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String description;

    @Min(1)
    private int quantite;

    @DecimalMin("0.0")
    private BigDecimal prixUnitaireHT;

    @Enumerated(EnumType.STRING)
    private TauxTVA tauxTVA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facture_id")
    @JsonIgnore
    private Facture facture;
}


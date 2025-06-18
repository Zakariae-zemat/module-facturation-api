package com.arimayi.facturation.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Client client;

    private LocalDate date = LocalDate.now();

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true)
    @Size(min = 1, message = "La facture doit contenir au moins une ligne")
    private List<LigneFacture> lignes;

    private BigDecimal totalHT;
    private BigDecimal totalTVA;
    private BigDecimal totalTTC;
}

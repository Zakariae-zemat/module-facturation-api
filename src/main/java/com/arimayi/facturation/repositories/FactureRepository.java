package com.arimayi.facturation.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arimayi.facturation.entities.Facture;

public interface FactureRepository extends JpaRepository<Facture, Long> {
    List<Facture> findByClientId(Long clientId);

    List<Facture> findByDate(LocalDate date);

    List<Facture> findByClientIdAndDate(Long clientId, LocalDate date);
}
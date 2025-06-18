package com.arimayi.facturation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arimayi.facturation.entities.LigneFacture;

public interface LigneFactureRepository extends JpaRepository<LigneFacture, Long> {
}

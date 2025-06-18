package com.arimayi.facturation.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.arimayi.facturation.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}

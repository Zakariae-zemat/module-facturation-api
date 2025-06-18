package com.arimayi.facturation.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nom;

    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "\\d{14}")
    private String siret;

    private LocalDate dateCreation = LocalDate.now();
}
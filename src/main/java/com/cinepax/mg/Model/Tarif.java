package com.cinepax.mg.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "tarif")
public class Tarif {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String idTarif;
    String description;
    double heureDebut;
    double heureFin;

    @Enumerated(EnumType.STRING)
    private Age age;

    double montant;
}

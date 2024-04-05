package com.cinepax.mg.Model;

import com.cinepax.mg.Exception.ValeurInvalideException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "salle")
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String idSalle;
    String nom;
    double capacite;
    int etat;


    public Salle(String idSalle, String nom, double capacite,int etat) throws ValeurInvalideException {
        this.idSalle = idSalle;
        this.setNom(nom);
        this.setCapacite(capacite);
        this.etat=etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public void setIdSalle(String idSalle) {
        this.idSalle = idSalle;
    }

    public void setNom(String nom) throws ValeurInvalideException {
        if(nom.equals(" ")){
            throw new ValeurInvalideException("Nom salle");
        }
        this.nom = nom;
    }

    public void setCapacite(double capacite) throws ValeurInvalideException {
        if(capacite<0){
            throw new ValeurInvalideException("Capacite salle");
        }
        this.capacite = capacite;
    }
}

package com.cinepax.mg.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "dataCsv")
public class DataCsv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String numSeance;
    String film;
    String categorie;
    String salle;
    Date daty;
    LocalTime heure;

    public void setId(int id) {
        this.id = id;
    }
    public void setNumSeance(String numSeance) {
        this.numSeance = numSeance;
    }
    public void setFilm(String film) {
        this.film = film;
    }
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
    public void setSalle(String salle) {
        this.salle = salle;
    }
    public void setDaty(Date daty) {
        this.daty = daty;
    }
    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }
}

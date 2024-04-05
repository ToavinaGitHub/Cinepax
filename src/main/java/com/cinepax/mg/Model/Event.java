package com.cinepax.mg.Model;


import com.cinepax.mg.Exception.ValeurInvalideException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String idEvent;

    @ManyToOne
            @JoinColumn(name = "idFilm" ,nullable = false)
    Film film;
    @ManyToOne
    @JoinColumn(name = "idSalle" ,nullable = false)
    Salle salle;
    Timestamp heure;
    Date date;
    double prix;
    int etat;

    public Event(String idEvent, Film film, Salle salle, Timestamp heure, Date date, double prix, int etat) throws ValeurInvalideException {
        this.idEvent = idEvent;
        this.film = film;
        this.salle = salle;
        this.heure = heure;
        this.date = date;
        this.setPrix(prix);
        this.etat = etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public void setHeure(Timestamp heure) {
        this.heure = heure;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPrix(double prix) throws ValeurInvalideException {
        if(prix<0){
            throw new ValeurInvalideException("Prix event");
        }
        this.prix = prix;
    }
}

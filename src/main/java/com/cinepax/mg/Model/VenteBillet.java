package com.cinepax.mg.Model;


import com.cinepax.mg.Exception.ValeurInvalideException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "vente_billet")
public class VenteBillet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String idVenteBillet;
    @ManyToOne
    @JoinColumn(name = "idEvent" ,nullable = false)
    Event event;
    double prix;
    double nombre;
    @Column(nullable = true)
    double montant;

    @Column(nullable = true)
    String places;
    Timestamp dateVente;

    // TODO: add column

    @ManyToOne
            @JoinColumn(name = "idTarif" ,nullable = true)
    Tarif tarif;

    int etat;

    public String getMoneyVersion(double vola){
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(vola);
    }

    public VenteBillet(String idVenteBillet, Event event, double prix, double nombre, double montant, String places, Timestamp dateVente, int etat) throws ValeurInvalideException {
        this.idVenteBillet = idVenteBillet;
        this.event = event;
        this.setPrix(prix);
        this.setNombre(nombre);
        this.setMontant(montant);
        this.places = places;
        this.dateVente = dateVente;
        this.etat = etat;
    }



    public void setTarif(Tarif tarif) {
        this.tarif = tarif;
    }

    public void setIdVenteBillet(String idVenteBillet) {
        this.idVenteBillet = idVenteBillet;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setPrix(double prix) throws ValeurInvalideException {
        if(prix<0){
            throw new ValeurInvalideException("Prix billet");
        }
        this.prix = prix;
    }

    public void setNombre(double nombre) throws ValeurInvalideException {
        if(nombre<=0){
            throw new ValeurInvalideException("Nombre billet");
        }
        this.nombre = nombre;
    }

    public void setMontant(double montant) throws ValeurInvalideException {
        if(montant<0){
            throw new ValeurInvalideException("Montant billet");
        }
        this.montant = montant;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public void setDateVente(Timestamp dateVente) {
        this.dateVente = dateVente;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }
}




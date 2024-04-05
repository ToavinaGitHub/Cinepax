package com.cinepax.mg.Model;


import com.cinepax.mg.Exception.ValeurInvalideException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor

@Getter
@Entity
@Table(name = "prix_produit")
public class PrixProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idPrixProduit;

    @ManyToOne
            @JoinColumn(name = "idProduit" , nullable = false)
    Produit produit;
    double prix;
    Date daty;
    int etat;

    public PrixProduit(int idPrixProduit, Produit produit, double prix, Date daty,int etat) throws ValeurInvalideException {
        this.idPrixProduit = idPrixProduit;
        this.produit = produit;
        this.setPrix(prix);
        this.daty = daty;
        this.etat = etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public void setIdPrixProduit(int idPrixProduit) {
        this.idPrixProduit = idPrixProduit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public void setPrix(double prix) throws ValeurInvalideException {
        if(prix<0){
            throw new ValeurInvalideException("Prix produit");
        }
        this.prix = prix;
    }

    public void setDaty(Date daty) {
        this.daty = daty;
    }
}

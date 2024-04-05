package com.cinepax.mg.Model;


import com.cinepax.mg.Exception.ValeurInvalideException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor

@Getter
@Entity
@Table(name = "produit")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String idProduit;
    String libelle;
    double prix;
    int etat;


    public Produit(String idProduit, String libelle, double prix, int etat) throws ValeurInvalideException {
        this.idProduit = idProduit;
        this.setLibelle(libelle);
        this.setPrix(prix);
        this.etat = etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public void setIdProduit(String idProduit) {
        this.idProduit = idProduit;
    }

    public void setLibelle(String libelle) throws ValeurInvalideException {
        if(libelle.equals(" ")){
            throw new ValeurInvalideException("Libelle produit");
        }
        this.libelle = libelle;
    }

    public void setPrix(double prix) throws ValeurInvalideException {
        if(prix<0){
            throw new ValeurInvalideException("Prix produit");
        }
        this.prix = prix;
    }
}

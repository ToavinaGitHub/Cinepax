package com.cinepax.mg.Model;


import com.cinepax.mg.Exception.ValeurInvalideException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor

@Getter
@Entity
@Table(name = "transaction_produit")
public class TransactionProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String idTransactionProduit;

    @ManyToOne
            @JoinColumn(name = "idProduit" ,nullable = false)
    Produit produit;
    double quantite;
    double pu;
    double montant;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    Timestamp daty;
    int type;
    int etat;

    public TransactionProduit(String idTransactionProduit, Produit produit, double quantite, double pu, double montant, Timestamp daty, int etat) throws ValeurInvalideException {
        this.idTransactionProduit = idTransactionProduit;
        this.produit = produit;
        this.setQuantite(quantite);
        this.setPu(pu);
        this.setMontant(montant);
        this.daty = daty;
        this.etat = etat;
    }

    public TransactionProduit(String idTransactionProduit, Produit produit, double quantite, double pu, double montant, Timestamp daty, int type, int etat) {
        this.idTransactionProduit = idTransactionProduit;
        this.produit = produit;
        this.quantite = quantite;
        this.pu = pu;
        this.montant = montant;
        this.daty = daty;
        this.type = type;
        this.etat = etat;
    }

    public void setIdTransactionProduit(String idTransactionProduit) {
        this.idTransactionProduit = idTransactionProduit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public void setQuantite(double quantite) throws ValeurInvalideException {
        if(quantite<0){
            throw  new ValeurInvalideException("Quantite transaction produit");
        }
        this.quantite = quantite;
    }

    public void setPu(double pu) throws ValeurInvalideException {
        if(pu<0){
            throw  new ValeurInvalideException("Pu transaction produit");
        }
        this.pu = pu;
    }

    public void setMontant(double montant) throws ValeurInvalideException {
        if(montant<0){
            throw  new ValeurInvalideException("Montant transaction produit");
        }
        this.montant = montant;
    }

    public void setDaty(Timestamp daty) {
        this.daty = daty;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public void setType(int type) {
        this.type = type;
    }
}

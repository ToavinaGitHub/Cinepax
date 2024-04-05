package com.cinepax.mg.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "place_salle")
public class PlaceSalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idPlaceSalle;
    String range;
    String numero;

    @ManyToOne
            @JoinColumn(name = "idSalle" ,nullable = false)
    Salle salle;
    int etat;


    public PlaceSalle(int idPlaceSalle, String range, String numero, Salle salle, int etat) {
        this.idPlaceSalle = idPlaceSalle;
        this.setRange(range);
        this.setNumero(numero);
        this.salle = salle;
        this.etat = etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public void setIdPlaceSalle(int idPlaceSalle) {
        this.idPlaceSalle = idPlaceSalle;
    }

    public void setRange(String range) {
        if(range.equals(" ")){
            throw new RuntimeException("range");
        }
        this.range = range;
    }

    public void setNumero(String numero) {
        if(numero.equals(" ")){
            throw new RuntimeException("numero");
        }
        this.numero = numero;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }
}

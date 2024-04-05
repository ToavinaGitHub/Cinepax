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
@Table(name = "genre_film")
public class GenreFilm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String idGenre;
    String libelle;
    int etat;

    public void setIdGenre(String idGenre) {
        this.idGenre = idGenre;
    }

    public GenreFilm(String idGenre, String libelle, int etat) throws Exception {
        this.idGenre = idGenre;
        this.setLibelle(libelle);
        this.etat = etat;
    }

    public void setLibelle(String libelle) throws Exception {
        if(libelle.equals(" ")){
            throw new ValeurInvalideException("Libelle genre film");
        }
        this.libelle = libelle;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }
}

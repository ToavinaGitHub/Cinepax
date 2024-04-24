package com.cinepax.mg.Model;


import com.cinepax.mg.Exception.ValeurInvalideException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
@Entity
@Table(name = "film")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String idFilm;
    String titre;
    String description;
    double duree;


    String sary;

    int etat;
    @ManyToOne
    @JoinColumn(name = "idGenreFilm" , nullable = false)
    GenreFilm genreFilm;

    public Film(String idFilm, String titre, String description, double duree, String sary, GenreFilm genreFilm ,int etat) throws ValeurInvalideException {
        this.idFilm = idFilm;
        this.setTitre(titre);
        this.setDescription(description);
        this.setDuree(duree);
        this.sary = sary;
        this.genreFilm = genreFilm;
        this.etat = etat;
    }

    public void setIdFilm(String idFilm) {
        this.idFilm = idFilm;
    }

    public void setTitre(String titre) throws ValeurInvalideException {
        if(titre.equals(" ")){
            throw new ValeurInvalideException("Titre");
        }
        this.titre = titre;
    }

    public void setDescription(String description) throws ValeurInvalideException {
        if(description.equals(" ")){
            throw new ValeurInvalideException("Description");
        }
        this.description = description;
    }

    public void setDuree(double duree) throws ValeurInvalideException {
        if(duree<0){
            throw new ValeurInvalideException("Duree");
        }
        this.duree = duree;
    }

    public void setSary(String sary) {
        this.sary = sary;
    }

    public void setGenreFilm(GenreFilm genreFilm) {
        this.genreFilm = genreFilm;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }
}

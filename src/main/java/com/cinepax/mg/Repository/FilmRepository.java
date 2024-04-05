package com.cinepax.mg.Repository;


import com.cinepax.mg.Model.Film;
import com.cinepax.mg.view.V_stats_film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FilmRepository extends CrudRepository<Film, String> , JpaRepository<Film, String> {


    Page<Film> findFilmByEtatAndTitreContainingIgnoreCase(int etat, String key, Pageable pageable);

    List<Film> findFilmByEtat(int etat);

    Page<Film> findFilmByEtat(int etat,Pageable p);

    @Query(value = "SELECT v.id_film,v.titre,v.montant,v.quantite from v_stats_film v",nativeQuery = true)
    List<V_stats_film> getStatByIdFilm();



}

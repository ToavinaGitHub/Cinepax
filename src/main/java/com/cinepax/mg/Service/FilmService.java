package com.cinepax.mg.Service;


import com.cinepax.mg.Model.Film;
import com.cinepax.mg.Repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    @Autowired
    FilmRepository filmRepository;

    public Page<Film> rechercheMultiMot(String recherche, Pageable pageable) {
        String[] mots = recherche.split("\\s+");
        Set<Film> resultat = new HashSet<>(); // Utiliser un ensemble pour éviter les doublons

        // Recherche pour chaque mot individuel
        for (String mot : mots) {
            Page<Film> motsResultatPage = filmRepository.rechercheMultiMot(mot, pageable);
            resultat.addAll(motsResultatPage.getContent());
        }

        // Convertir l'ensemble en une liste pour pouvoir le passer à PageImpl
        List<Film> resultatList = new ArrayList<>(resultat);

        return new PageImpl<>(resultatList, pageable, resultatList.size());
    }


}

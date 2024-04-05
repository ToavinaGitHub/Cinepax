package com.cinepax.mg.Repository;


import com.cinepax.mg.Model.GenreFilm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface GenreFilmRepository extends CrudRepository<GenreFilm, String> , JpaRepository<GenreFilm, String> {









}

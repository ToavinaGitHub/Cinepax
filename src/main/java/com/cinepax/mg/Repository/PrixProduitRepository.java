package com.cinepax.mg.Repository;


import com.cinepax.mg.Model.PrixProduit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PrixProduitRepository extends CrudRepository<PrixProduit, Integer> , JpaRepository<PrixProduit, Integer> {









}

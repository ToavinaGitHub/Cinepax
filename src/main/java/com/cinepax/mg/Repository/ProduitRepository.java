package com.cinepax.mg.Repository;


import com.cinepax.mg.Model.Produit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProduitRepository extends CrudRepository<Produit, String> , JpaRepository<Produit, String> {









}

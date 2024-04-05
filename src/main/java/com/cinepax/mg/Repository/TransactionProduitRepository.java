package com.cinepax.mg.Repository;


import com.cinepax.mg.Model.TransactionProduit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface TransactionProduitRepository extends CrudRepository<TransactionProduit, String> , JpaRepository<TransactionProduit, String> {









}

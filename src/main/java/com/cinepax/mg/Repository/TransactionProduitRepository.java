package com.cinepax.mg.Repository;


import com.cinepax.mg.Model.Film;
import com.cinepax.mg.Model.TransactionProduit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface TransactionProduitRepository extends CrudRepository<TransactionProduit, String> , JpaRepository<TransactionProduit, String> {



    @Query("SELECT t FROM TransactionProduit t WHERE DATE(t.daty) = DATE(?2) and t.etat= ?1")
    Page<TransactionProduit> getByEtatDate(int etat, Date key, Pageable pageable);

    List<TransactionProduit> findTransactionProduitByEtat(int etat);

    Page<TransactionProduit> findTransactionProduitByEtat(int etat,Pageable p);







}

package com.cinepax.mg.Repository;


import com.cinepax.mg.Model.Utilisateur;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UtilisateurRepository extends CrudRepository<Utilisateur, String> , JpaRepository<Utilisateur, String> {


    public Utilisateur findUtilisateurByEmailAndPassword(String email,String password);

    public Utilisateur findUtilisateurByEmail(String email);





}

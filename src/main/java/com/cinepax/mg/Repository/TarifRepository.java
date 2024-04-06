package com.cinepax.mg.Repository;

import com.cinepax.mg.Model.Tarif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TarifRepository extends JpaRepository<Tarif,String> {

    @Query(value = "SELECT * from tarif where heure_debut <= extract(hour from now()) and heure_fin > extract(hour from now()) and age= ?1 LIMIT 1",nativeQuery = true)
    Tarif getTarifByHeureAge(String age);
}

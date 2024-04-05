package com.cinepax.mg.Repository;


import com.cinepax.mg.Model.Salle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface SalleRepository extends CrudRepository<Salle, String> , JpaRepository<Salle, String> {

    @Query(value = "SELECT DISTINCT range from place_salle WHERE id_salle= ?1",nativeQuery = true)
    List<String> getRangeCountBySalle(String idSalle);
}

package com.cinepax.mg.Repository;


import com.cinepax.mg.Model.VenteBillet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface VenteBilletRepository extends CrudRepository<VenteBillet, String> , JpaRepository<VenteBillet, String> {


    @Transactional
    @Modifying
    @Query(value = "INSERT INTO genre_film(etat, libelle)  (select 1,dt.categorie from data_csv dt left join genre_film g on dt.categorie=g.libelle where g.id_genre is null group by dt.categorie )",nativeQuery = true)
    public void insertGenreByCsv();
}

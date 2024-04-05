package com.cinepax.mg.Repository;


import com.cinepax.mg.Model.PlaceSalle;
import com.cinepax.mg.Model.Salle;
import com.cinepax.mg.view.V_place_event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PlaceSalleRepository extends CrudRepository<PlaceSalle, Integer> , JpaRepository<PlaceSalle, Integer> {


    @Query(value = "SELECT id_place_salle,range,numero,dispo,id_vente_billet from v_place_event where range= ?1 and numero= ?2 and id_event= ?3 ",nativeQuery = true)
    public V_place_event getPlaceSallePar(String range, String numero,String idEvent);

}

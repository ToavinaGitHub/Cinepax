package com.cinepax.mg.Repository;


import com.cinepax.mg.Model.PlaceSalle;
import com.cinepax.mg.Model.Salle;
import com.cinepax.mg.view.V_place_event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PlaceSalleRepository extends CrudRepository<PlaceSalle, Integer> , JpaRepository<PlaceSalle, Integer> {


    @Query(value = "SELECT r1.id_place_salle,r2.id_vente_billet,r1.range,r1.numero,r1.id_event,r1.id_salle,r2.id_event,\n" +
            "       CASE\n" +
            "            when r2.id_event is null then 1\n" +
            "            when r2.id_event is not null then 0\n" +
            "        END as dispo\n" +
            "from\n" +
            "(select * from v_temp_1 where id_event=?3 ) r1\n" +
            "left join\n" +
            "(select * from v_temp_2 where id_event=?3) as r2\n" +
            "on r1.id_place_salle=r2.id_place_salle where range= ?1 and numero= ?2",nativeQuery = true)
    public V_place_event getPlaceSallePar(String range, String numero,String idEvent);

}

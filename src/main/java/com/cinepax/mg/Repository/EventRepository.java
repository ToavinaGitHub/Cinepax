package com.cinepax.mg.Repository;


import com.cinepax.mg.Model.Event;
import com.cinepax.mg.view.V_place_event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface EventRepository extends CrudRepository<Event, String> , JpaRepository<Event, String> {

    public List<Event> findEventByEtat(int etat);

    @Query(value = "SELECT id_place_salle,range,numero,dispo,id_vente_billet from v_place_event where id_event= ?1 ",nativeQuery = true)
    public List<V_place_event> getPlaceEvent(String idEvent);

    @Query(value = "SELECT id_place_salle,range,numero,dispo,id_vente_billet from v_place_event where id_event= ?1 and range= ?2 ",nativeQuery = true)
    public List<V_place_event> getPlaceEventRange(String idEvent,String range);
}

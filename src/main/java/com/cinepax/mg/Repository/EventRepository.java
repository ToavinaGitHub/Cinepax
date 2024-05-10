package com.cinepax.mg.Repository;


import com.cinepax.mg.Model.Event;
import com.cinepax.mg.Model.Film;
import com.cinepax.mg.view.V_place_event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface EventRepository extends CrudRepository<Event, String> , JpaRepository<Event, String> {

    @Query("SELECT t FROM Event t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ORDER BY t.prix ASC ")
    Page<Event> getByDateAsc(Date key,int etat, Pageable pageable);

    @Query("SELECT t FROM Event t WHERE DATE(t.date) = DATE(?1) and t.etat= ?2 ORDER BY t.prix DESC ")
    Page<Event> getByDateDesc(Date key,int etat, Pageable pageable);

    Page<Event> findEventByEtatOrderByPrixAsc(int etat,Pageable p);

    Page<Event> findEventByEtatOrderByPrixDesc(int etat,Pageable p);

    public List<Event> findEventByEtat(int etat);

    @Query(value = "SELECT r1.id_place_salle,r2.id_vente_billet,r1.range,r1.numero,r1.id_event,r1.id_salle,r2.id_event,\n" +
            "       CASE\n" +
            "            when r2.id_event is null then 1\n" +
            "            when r2.id_event is not null then 0\n" +
            "        END as dispo\n" +
            "from\n" +
            "(select * from v_temp_1 where id_event= ?1 ) r1\n" +
            "left join\n" +
            "(select * from v_temp_2 where id_event= ?1) as r2\n" +
            "on r1.id_place_salle=r2.id_place_salle",nativeQuery = true)
    public List<V_place_event> getPlaceEvent(String idEvent);

    @Query(value = "SELECT r1.id_place_salle,r2.id_vente_billet,r1.range,r1.numero,r1.id_event,r1.id_salle,r2.id_event,\n" +
            "       CASE\n" +
            "            when r2.id_event is null then 1\n" +
            "            when r2.id_event is not null then 0\n" +
            "        END as dispo\n" +
            "from\n" +
            "(select * from v_temp_1 where id_event=?1 ) r1\n" +
            "left join\n" +
            "(select * from v_temp_2 where id_event=?1) as r2\n" +
            "on r1.id_place_salle=r2.id_place_salle where range= ?2",nativeQuery = true)
    public List<V_place_event> getPlaceEventRange(String idEvent,String range);




}

package com.cinepax.mg.Service;

import com.cinepax.mg.Exception.ValeurInvalideException;
import com.cinepax.mg.Model.Event;
import com.cinepax.mg.Model.PlaceSalle;
import com.cinepax.mg.Model.Salle;
import com.cinepax.mg.Repository.EventRepository;
import com.cinepax.mg.Repository.PlaceSalleRepository;
import com.cinepax.mg.Repository.SalleRepository;
import com.cinepax.mg.view.V_place_event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PlaceService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    SalleRepository salleRepository;

    @Autowired
    PlaceSalleRepository placeSalleRepository;

    public HashMap<String, List<V_place_event>> getPlaceParRange(String event){

        Event e = eventRepository.findById(event).get();
        List<String> allRange = salleRepository.getRangeCountBySalle(e.getSalle().getIdSalle());
        HashMap<String, List<V_place_event>> places = new HashMap<>();

        for (String r:allRange
             ) {
            places.put(r,eventRepository.getPlaceEventRange(event,r));
        }
        return places;
    }

    public List<V_place_event> splitByText(String idEvent, String places) throws ValeurInvalideException {
        String[] all = places.split(";");
        List<V_place_event> res = new ArrayList<>();
        for (String a:all
             ) {
            String[] temp = a.split("_");
            try{
                V_place_event t = placeSalleRepository.getPlaceSallePar(temp[0],temp[1],idEvent);
                res.add(t);
            }catch (Exception e){
                throw new ValeurInvalideException("Places");
            }
        }
        return res;
    }


}

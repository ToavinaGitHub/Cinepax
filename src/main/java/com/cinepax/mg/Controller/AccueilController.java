package com.cinepax.mg.Controller;


import com.cinepax.mg.Model.Event;
import com.cinepax.mg.Repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/v1/accueil")
public class AccueilController {
    @Autowired
    EventRepository eventRepository;

    @GetMapping("")
    public String index(Model model){
        List<Event> allEvent = eventRepository.findEventByEtat(1);
        model.addAttribute("allEvent" , allEvent);
        return "Accueil/index";
    }
}

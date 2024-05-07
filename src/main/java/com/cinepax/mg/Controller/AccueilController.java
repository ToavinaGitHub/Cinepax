package com.cinepax.mg.Controller;


import com.cinepax.mg.Model.Event;
import com.cinepax.mg.Model.VenteBillet;
import com.cinepax.mg.Repository.ContentRepository;
import com.cinepax.mg.Repository.EventRepository;
import com.cinepax.mg.Service.ContentService;
import com.cinepax.mg.view.Language;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/v1/accueil")
public class AccueilController {
    @Autowired
    EventRepository eventRepository;

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    ContentService contentService;



    @GetMapping("")
    public String index(@RequestParam(name = "lang",required = false,defaultValue = "fr") String lang, HttpSession session, Model model) throws IOException {
        HashMap<String,String> allLanguage = contentService.getContentByLanguage(lang);
        HashMap<String,String> fileLangs = contentService.getContentFromFile(lang);
        allLanguage.putAll(fileLangs);
        session.setAttribute("lang",lang);
        List<Event> allEvent = eventRepository.findEventByEtat(1);

        VenteBillet v = new VenteBillet();

        model.addAttribute("venteBillet",v);
        model.addAttribute("allEvent" , allEvent);
        model.addAttribute("content",allLanguage);
        return "Accueil/index";
    }

}

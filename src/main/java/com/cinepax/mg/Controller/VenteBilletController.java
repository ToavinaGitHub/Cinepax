package com.cinepax.mg.Controller;


import com.cinepax.mg.Exception.ValeurInvalideException;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.cinepax.mg.Model.*;
import com.cinepax.mg.Repository.*;
import com.cinepax.mg.Service.PlaceService;
import com.cinepax.mg.view.V_place_event;
import com.cinepax.mg.view.V_stats_film;
import jakarta.servlet.ServletContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/v1/venteBillet")
public class VenteBilletController {
    @Autowired
    VenteBilletRepository venteBilletRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    SalleRepository salleRepository;

    @Autowired
    DetailsVenteBilletRepository detailsVenteBilletRepository;

    @Autowired
    TarifRepository tarifRepository;

    @Autowired
    PlaceService placeService;

    @Autowired
    ServletContext servletContext;
    private final TemplateEngine templateEngine;
    public VenteBilletController(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @GetMapping("/{idEvent}")
    public String index(@PathVariable("idEvent")String idEvent, Model model){
        Event e = eventRepository.findById(idEvent).get();
        HashMap<String,List<V_place_event>> places = placeService.getPlaceParRange(idEvent);

        Age[] ages = Age.values();
        model.addAttribute("ages",ages);

        model.addAttribute("places" , places);
        model.addAttribute("event" ,e);


        return "VenteBillet/index";
    }

    @PostMapping("")
    @Transactional
    public String vente(@RequestParam("idEvent") String idEvent, @RequestParam("places")String places ,@RequestParam("age") String age , Model model,RedirectAttributes redirectAttributes){
        Event e = eventRepository.findById(idEvent).get();
        VenteBillet v = new VenteBillet();
        System.out.println(1);
        List<DetailsVenteBillet> dets = new ArrayList<>();
        try {
            List<V_place_event> vPlaceEvents = placeService.splitByText(idEvent,places);

            v.setEvent(e);
            v.setPlaces(places);
            v.setNombre(vPlaceEvents.size());
            v.setMontant(v.getPrix()*v.getNombre());
            v.setEtat(1);
            v.setDateVente(new Timestamp(System.currentTimeMillis()));
            System.out.println(2);

            Tarif t = tarifRepository.getTarifByHeureAge(age);
            v.setTarif(t);

            v.setPrix(t.getMontant());

            venteBilletRepository.save(v);
            String mess = "";
            for (V_place_event vp:vPlaceEvents
                 ) {
                if(vp.getDispo()==0){
                    throw new ValeurInvalideException("Place non dispo");
                }
                System.out.println(3);
                PlaceSalle placeSalle = new PlaceSalle(vp.getId_place_salle(),vp.getRange(),vp.getNumero(),e.getSalle(),1);
                DetailsVenteBillet d = new DetailsVenteBillet();
                d.setVenteBillet(v);
                d.setPlaceSalle(placeSalle);
                dets.add(d);
                detailsVenteBilletRepository.save(d);
                mess = mess.concat(" , "+vp.getRange()+"_"+vp.getNumero());
            }
            redirectAttributes.addFlashAttribute("message" , "Achat de :"+mess);
            System.out.println(4);
        } catch (ValeurInvalideException ex) {
            redirectAttributes.addFlashAttribute("error" , ex.getMessage());
            return "redirect:/v1/venteBillet/"+idEvent;
        }

        HashMap<String,List<V_place_event>> placesTemp = placeService.getPlaceParRange(idEvent);

        Age[] ages = Age.values();
        model.addAttribute("ages",ages);

        model.addAttribute("places" , placesTemp);
        model.addAttribute("event" ,e);

        model.addAttribute("details" , dets);

        return "VenteBillet/details";
       // return "redirect:/v1/venteBillet/"+idEvent;
    }

    @GetMapping("/statParFilm")
    public String statFilm(Model model){
        List<V_stats_film> all = filmRepository.getStatByIdFilm();
        model.addAttribute("stats" , all);
        return "Statistique/statParFilm";
    }

    @GetMapping("/pdf")
    public void toPdf(@RequestParam("idDetails") String idDetails,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes,Model model) throws IOException {
        DetailsVenteBillet d = detailsVenteBilletRepository.findById(idDetails).get();

        Context context = new Context();
        context.setVariable("det", d);
        String orderHtml = templateEngine.process("VenteBillet/detailsPdf", context);

        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");

        /* Convert HTML to PDF */

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(orderHtml, target, converterProperties);

        response.setContentType("application/pdf");

        String nom = "billet"+d.getIdDetailsVenteBillet()+".pdf";
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+nom+"\"");

        response.getOutputStream().write(target.toByteArray());
    }
}

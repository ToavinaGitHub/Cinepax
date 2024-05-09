package com.cinepax.mg.Controller;

import com.cinepax.mg.Repository.FilmRepository;
import com.cinepax.mg.Repository.SalleRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cinepax.mg.Model.Event;
import com.cinepax.mg.Repository. EventRepository;

import org.springframework.ui.Model;

@RequestMapping("/v1/event")
@Controller
public class EventController {

    @Autowired
    private EventRepository  eventRepository;

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    SalleRepository salleRepository;

    @GetMapping("")
    public String index(Model model , @RequestParam(name = "keyword" ,required = false ,defaultValue ="") String key,
      @RequestParam(defaultValue = "1" , required = false ,name = "page") int page, @RequestParam(defaultValue = "3" , required = false ,name = "size") int size , @RequestParam(defaultValue = "-1" ,name = "tri") int tri ) throws ParseException {
        List<Event> events = new ArrayList<Event>();

        Pageable pageable =PageRequest.of(page-1,size);

        Page<Event> pageCateg;

        if(key.compareTo("")==0){
            if(tri==-1){
                pageCateg = eventRepository.findEventByEtatOrderByPrixAsc(1,pageable);
            }else{
                pageCateg = eventRepository.findEventByEtatOrderByPrixDesc(1,pageable);
            }
        }else {
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            Date d = s.parse(key);
            if (tri==-1){
                pageCateg =  eventRepository.getByDateAsc(d,1,pageable);
            }else {
                pageCateg =  eventRepository.getByDateDesc(d,1,pageable);
            }

        }

        Event c = new Event();

        events = pageCateg.getContent();


        model.addAttribute("salles" , salleRepository.findSalleByEtat(1));
        model.addAttribute("films" ,filmRepository.findFilmByEtat(1));

        model.addAttribute("keyword" , key);
        model.addAttribute("event" , c);
        model.addAttribute("events", events);

        model.addAttribute("currentPage", pageCateg.getNumber() + 1);
        model.addAttribute("totalItems", pageCateg.getTotalElements());
        model.addAttribute("totalPages", pageCateg.getTotalPages());
        model.addAttribute("pageSize", size);

        return "Event/index";
    }

    @PostMapping("")
    public String save(@Valid Event event ,@RequestParam("lera")String lera, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            String message = "";
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/event";
        }
        try{


            event.setEtat(1);
            String dateTimeString = event.getDate()+" "+lera;
            System.out.println(dateTimeString);
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy HH:mm", Locale.ENGLISH);

            Date date = inputFormat.parse(dateTimeString);

            Timestamp t = new Timestamp(date.getTime());
            event.setHeure(t);
            eventRepository.save(event);
            redirectAttributes.addFlashAttribute("success", "Event ajoutée avec succès");
            redirectAttributes.addFlashAttribute("message" , "Insertion avec succes");
        }catch(Exception e){
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
        }

        return "redirect:/v1/event";
    }
    @GetMapping("/{id}")
    public String editTutorial(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Event t = eventRepository.findById(id).get();

            model.addAttribute("event", t);
            model.addAttribute("pageTitle", "Edit Event (ID: " + id + ")");

            //List<Event> events = eventRepository.findAll();

            List<Event> events = eventRepository.findEventByEtat(1);

            model.addAttribute("salles" , salleRepository.findSalleByEtat(1));
            model.addAttribute("films" ,filmRepository.findFilmByEtat(1));

            model.addAttribute("isUpdate" , 1);
            model.addAttribute("events", events);
            return "Event/index";
        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
            return "redirect:/v1/event";
        }
    }

    @GetMapping("/delOption")
    public String delete(@RequestParam String id , RedirectAttributes redirectAttributes){
        Event ct =  eventRepository.findById(id).get();
        //eventRepository.delete(ct);
        ct.setEtat(0);
        eventRepository.save(ct);

        redirectAttributes.addFlashAttribute("message" , "Supprimé avec succés");
        return "redirect:/v1/event";
    }

    @PostMapping("/edit")
    public String modifier(@Valid @ModelAttribute Event event,@RequestParam("lera")String lera,BindingResult bindingResult,RedirectAttributes redirectAttributes){
        String message = "";
        if(bindingResult.hasErrors()){
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
    
            return "redirect:/v1/event";
        }
        try{
            String dateTimeString = event.getDate()+" "+lera;
            System.out.println(dateTimeString);
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy HH:mm", Locale.ENGLISH);

            Date date = inputFormat.parse(dateTimeString);

            Timestamp t = new Timestamp(date.getTime());
            event.setHeure(t);
            eventRepository.save(event);
        }catch (Exception e){
            message += e.getMessage()+" ; ";
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/event";
        }

        redirectAttributes.addFlashAttribute("message" , "Modification avec succes");
        return "redirect:/v1/event";
    }




}

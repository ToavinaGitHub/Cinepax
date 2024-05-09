package com.cinepax.mg.Controller;

import com.cinepax.mg.Model.GenreFilm;
import com.cinepax.mg.Repository.GenreFilmRepository;
import com.cinepax.mg.Service.FilmService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cinepax.mg.Model.Film;
import com.cinepax.mg.Repository. FilmRepository;

import org.springframework.ui.Model;

@RequestMapping("/v1/film")
@Controller
public class FilmController {

    @Autowired
    private FilmRepository  filmRepository;

    @Autowired
    FilmService filmService;

    @Autowired
    GenreFilmRepository genreFilmRepository;

    @GetMapping("")
    public String index(Model model , @RequestParam(name = "keyword" ,required = false,defaultValue = "") String key,
      @RequestParam(defaultValue = "1" , required = false ,name = "page") int page, @RequestParam(defaultValue = "3" , required = false ,name = "size") int size)  {
        List<Film> films = new ArrayList<Film>();

        Pageable pageable =PageRequest.of(page-1,size);

        Page<Film> pageCateg;
        if(key.compareTo("")==0){
            pageCateg = filmRepository.findFilmByEtat(1,pageable);
        }else{
            pageCateg = filmService.rechercheMultiMot(key,pageable);
            //pageCateg =  filmRepository.findFilmByEtatAndTitreContainingIgnoreCase(1,key,pageable);
        }

        Film c = new Film();

        films = pageCateg.getContent();

        List<GenreFilm> genreFilms = genreFilmRepository.findAll();
        model.addAttribute("genres", genreFilms);

        model.addAttribute("keyword" , key);
        model.addAttribute("film" , c);
        model.addAttribute("films", films);

        model.addAttribute("currentPage", pageCateg.getNumber() + 1);
        model.addAttribute("totalItems", pageCateg.getTotalElements());
        model.addAttribute("totalPages", pageCateg.getTotalPages());
        model.addAttribute("pageSize", size);

        return "Film/index";
    }

    @Transactional
    @PostMapping("")
    public String save(@Valid Film film,@RequestParam("img") MultipartFile file , BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        System.out.println("hahaha");
        if(bindingResult.hasErrors()){
            String message = "";
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/film";
        }
        try{

            String uploadDir = "./sary";
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            if (!file.isEmpty()) {
                System.out.println(file.getOriginalFilename().lastIndexOf("."));
                String fileName = StringUtils.cleanPath(film.getIdFilm()+"_"+file.getOriginalFilename());
                Path uploadPath = Paths.get(uploadDir, fileName);
                try (OutputStream outputStream = Files.newOutputStream(uploadPath)) {
                    film.setSary(fileName);
                    outputStream.write(file.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            film.setEtat(1);
            filmRepository.save(film);
            redirectAttributes.addFlashAttribute("success", "Film ajoutée avec succès");
            redirectAttributes.addFlashAttribute("message" , "Insertion avec succes");
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", "Transaction echouée : "+e.getMessage()
            );
        }
        return "redirect:/v1/film";
    }
    @GetMapping("/{id}")
    public String editTutorial(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Film t = filmRepository.findById(id).get();

            model.addAttribute("film", t);
            model.addAttribute("pageTitle", "Edit Film (ID: " + id + ")");

            //List<Film> films = filmRepository.findAll();

            List<Film> films = filmRepository.findFilmByEtat(1);

            List<GenreFilm> genreFilms = genreFilmRepository.findAll();
            model.addAttribute("genres", genreFilms);

            model.addAttribute("isUpdate" , 1);
            model.addAttribute("films", films);

            return "Film/index";
        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
            return "redirect:/v1/film";
        }
    }
    
    @GetMapping("/delOption")
    public String delete(@RequestParam String id , RedirectAttributes redirectAttributes){
        Film ct =  filmRepository.findById(id).get();
        //filmRepository.delete(ct);
        ct.setEtat(0);
        filmRepository.save(ct);

        redirectAttributes.addFlashAttribute("message" , "Supprimé avec succés");
        return "redirect:/v1/film";
    }

    @PostMapping("/edit")
    public String modifier(@Valid @ModelAttribute Film film,BindingResult bindingResult,RedirectAttributes redirectAttributes){
        String message = "";

        Film temp = filmRepository.findById(film.getIdFilm()).get();

        film.setSary(temp.getSary());
        if(bindingResult.hasErrors()){
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
    
            return "redirect:/v1/film";
        }
        try{
             filmRepository.save(film);
        }catch (Exception e){
            message += e.getMessage()+" ; ";
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/film";
        }

        redirectAttributes.addFlashAttribute("message" , "Modification avec succes");
        return "redirect:/v1/film";
    }

}

package com.cinepax.mg.Controller;


import com.cinepax.mg.Repository.ChiffreAffaireRepository;
import com.cinepax.mg.view.V_film_plus_vues_jour;
import com.cinepax.mg.view.V_montant_film_jour;
import com.cinepax.mg.view.V_montant_produit_jour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/v1/chiffreAffaire")
public class ChiffreAffaireController {

    @Autowired
    ChiffreAffaireRepository chiffreAffaireRepository;


    @GetMapping("/journalier")
    public String montantJour(Model model){
        List<V_montant_produit_jour> produits = chiffreAffaireRepository.getMontantProduitParJour();
        List<V_montant_film_jour> films = chiffreAffaireRepository.getMontantFilmParJour();
        List<V_film_plus_vues_jour> filmsVues = chiffreAffaireRepository.getFilmPlusVue();
        model.addAttribute("produits" , produits);
        model.addAttribute("films" , films);
        model.addAttribute("filmsVues" , filmsVues);
        return "ChiffreAffaire/Journalier";
    }
}

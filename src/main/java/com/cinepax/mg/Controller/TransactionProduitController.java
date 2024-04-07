package com.cinepax.mg.Controller;

import com.cinepax.mg.Model.Produit;
import com.cinepax.mg.Repository.ProduitRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cinepax.mg.Model.TransactionProduit;
import com.cinepax.mg.Repository. TransactionProduitRepository;

import org.springframework.ui.Model;

@RequestMapping("/v1/transactionProduit")
@Controller
public class TransactionProduitController {

    @Autowired
    private TransactionProduitRepository  transactionProduitRepository;

    @Autowired
    ProduitRepository produitRepository;

    @GetMapping("")
    public String index(Model model , @RequestParam(name = "keyword" ,required = false) String key,
                        @RequestParam(defaultValue = "1" , required = false ,name = "page") int page, @RequestParam(defaultValue = "3" , required = false ,name = "size") int size) throws ParseException {
        List<TransactionProduit> transactionProduits = new ArrayList<TransactionProduit>();

        Pageable pageable =PageRequest.of(page-1,size);

        Page<TransactionProduit> pageCateg;
        if(key==null){
            pageCateg = transactionProduitRepository.findTransactionProduitByEtat(1,pageable);
        }else{

            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            Date d = s.parse(key);
            pageCateg =  transactionProduitRepository.getByEtatDate(1,d,pageable);
        }

        TransactionProduit c = new TransactionProduit();

        transactionProduits = pageCateg.getContent();

        List<Produit> allP = produitRepository.findAll();
        model.addAttribute("produits",allP);
        model.addAttribute("lera",new Timestamp(new Date().getTime()));

        model.addAttribute("keyword" , key);
        model.addAttribute("transactionProduit" , c);
        model.addAttribute("transactionProduits", transactionProduits);

        model.addAttribute("currentPage", pageCateg.getNumber() + 1);
        model.addAttribute("totalItems", pageCateg.getTotalElements());
        model.addAttribute("totalPages", pageCateg.getTotalPages());
        model.addAttribute("pageSize", size);

        return "TransactionProduit/index";
    }

    @PostMapping("")
    public String save(@Valid TransactionProduit transactionProduit ,@RequestParam("lera") String lera, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            String message = "";
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/transactionProduit";
        }
        try{
            transactionProduit.setEtat(1);
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date d= s.parse(lera);
            Timestamp t = new Timestamp(d.getTime());
            transactionProduit.setDaty(t);
            transactionProduitRepository.save(transactionProduit);
            redirectAttributes.addFlashAttribute("success", "TransactionProduit ajoutée avec succès");
            redirectAttributes.addFlashAttribute("message" , "Insertion avec succes");
        }catch(Exception e){
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
        }

        return "redirect:/v1/transactionProduit";
    }
    @GetMapping("/{id}")
    public String editTutorial(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            TransactionProduit t = transactionProduitRepository.findById(id).get();

            model.addAttribute("transactionProduit", t);
            model.addAttribute("lera" ,t.getDaty());
            model.addAttribute("pageTitle", "Edit TransactionProduit (ID: " + id + ")");

            //List<TransactionProduit> transactionProduits = transactionProduitRepository.findAll();

            List<TransactionProduit> transactionProduits = transactionProduitRepository.findTransactionProduitByEtat(1);

            List<Produit> allP = produitRepository.findAll();
            model.addAttribute("produits",allP);

            model.addAttribute("isUpdate" , 1);
            model.addAttribute("transactionProduits", transactionProduits);
            return "TransactionProduit/index";
        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("error", "Transaction echouée");
            return "redirect:/v1/transactionProduit";
        }
    }

    @GetMapping("/delOption")
    public String delete(@RequestParam String id , RedirectAttributes redirectAttributes){
        TransactionProduit ct =  transactionProduitRepository.findById(id).get();
        //transactionProduitRepository.delete(ct);
        ct.setEtat(0);
        transactionProduitRepository.save(ct);

        redirectAttributes.addFlashAttribute("message" , "Supprimé avec succés");
        return "redirect:/v1/transactionProduit";
    }

    @PostMapping("/edit")
    public String modifier(@Valid @ModelAttribute TransactionProduit transactionProduit,@RequestParam("lera") String lera,BindingResult bindingResult,RedirectAttributes redirectAttributes){
        String message = "";
        if(bindingResult.hasErrors()){
            for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
                message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
            }
            redirectAttributes.addFlashAttribute("error", message);

            return "redirect:/v1/transactionProduit";
        }
        try{
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date d= s.parse(lera);
            Timestamp t = new Timestamp(d.getTime());
            transactionProduit.setDaty(t);
            transactionProduitRepository.save(transactionProduit);
        }catch (Exception e){
            message += e.getMessage()+" ; ";
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/v1/transactionProduit";
        }

        redirectAttributes.addFlashAttribute("message" , "Modification avec succes");
        return "redirect:/v1/transactionProduit";
    }

}

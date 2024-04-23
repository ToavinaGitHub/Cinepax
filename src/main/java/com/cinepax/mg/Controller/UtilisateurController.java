package com.cinepax.mg.Controller;

import com.cinepax.mg.Exception.ValeurInvalideException;
import com.cinepax.mg.Model.Utilisateur;

import com.cinepax.mg.Repository.UtilisateurRepository;
import com.cinepax.mg.Service.ContentService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class UtilisateurController {
	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	ContentService contentService;

	@GetMapping("/")
	public String login(Model model) throws ValeurInvalideException, IOException {
		Utilisateur u = new Utilisateur();
		u.setEmail("toavina@gmail.com");
		u.setPassword("toavina");
		model.addAttribute("utilisateur",u);

		contentService.getContentFromFile("fr");

		return "Auth/login";
	}
	@PostMapping("/login")
	public String login(Utilisateur u, RedirectAttributes redirectAttributes, HttpSession session){
		Utilisateur user = utilisateurRepository.findUtilisateurByEmailAndPassword(u.getEmail(),u.getPassword());
		if(user==null){
			redirectAttributes.addFlashAttribute("error" , "Mot de passe ou email non valide");
			return "redirect:/";
		}
		session.setAttribute("user" , user);
		return "redirect:/v1/accueil";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session,RedirectAttributes redirectAttributes){
		session.removeAttribute("user");
		redirectAttributes.addFlashAttribute("message" ,"Veuillez vous reconnecter");
		return "redirect:/";
	}
}

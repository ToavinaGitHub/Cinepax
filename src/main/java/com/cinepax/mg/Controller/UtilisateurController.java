package com.cinepax.mg.Controller;

import com.cinepax.mg.Exception.ValeurInvalideException;
import com.cinepax.mg.Model.Utilisateur;

import com.cinepax.mg.Repository.UtilisateurRepository;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class UtilisateurController {
	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@GetMapping("/")
	public String login(Model model) throws ValeurInvalideException {
		Utilisateur u = new Utilisateur();
		u.setEmail("toavina@gmail.com");
		u.setPassword("toavina");
		model.addAttribute("utilisateur",u);
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

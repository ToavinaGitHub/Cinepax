package com.cinepax.mg.Controller;

import com.cinepax.mg.Exception.ValeurInvalideException;
import com.cinepax.mg.Model.Utilisateur;

import com.cinepax.mg.Repository.UtilisateurRepository;
import com.cinepax.mg.Service.ContentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
public class UtilisateurController {
	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	ContentService contentService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

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
	public String login(Utilisateur u, RedirectAttributes redirectAttributes, HttpSession session, HttpServletRequest request){
		Utilisateur user  = utilisateurRepository.findUtilisateurByEmail(u.getEmail());

		if(user==null || !passwordEncoder.matches(u.getPassword(),user.getPassword())){
			redirectAttributes.addFlashAttribute("error" , "Mot de passe ou email non valide");
			return "redirect:/";
		}

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(u.getEmail(), u.getPassword());

		Authentication authentication = authenticationManager.authenticate(authRequest);
		SecurityContext sc = SecurityContextHolder.getContext();
		sc.setAuthentication(authentication);


		HttpSession s = request.getSession(true);
		session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);


		session.setAttribute("user" , user);

		return "redirect:/v1/accueil";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session,RedirectAttributes redirectAttributes,HttpServletRequest request){
		session.removeAttribute("user");
		HttpSession s = request.getSession(true);
		session.removeAttribute(SPRING_SECURITY_CONTEXT_KEY);
		redirectAttributes.addFlashAttribute("message" ,"Veuillez vous reconnecter");
		return "redirect:/";
	}

	@GetMapping("/error/403")
	public String error403(){
		return "Auth/403Error";
	}
	@GetMapping("/error/404")
	public String error404(){
		return "Auth/404Error";
	}
}

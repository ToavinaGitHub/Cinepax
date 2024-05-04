package com.cinepax.mg.security;

import com.cinepax.mg.Model.Utilisateur;
import com.cinepax.mg.Repository.UtilisateurRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UtilisateurRepository userRepository;

    public CustomUserDetailsService(UtilisateurRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur user = userRepository.findUtilisateurByEmail(email);
        if(user == null)
        {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }
}
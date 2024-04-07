package com.cinepax.mg.Auth;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.cinepax.mg.Model.Profil;
import com.cinepax.mg.Model.Utilisateur;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class SessionInvalide extends OncePerRequestFilter {
    private static final String LOGIN_URL = "/";

    private static final String SIMPLE_USER_PAGE = "/v1/accueil";
    private static final List<String> ALLOWED_URIS= Arrays.asList( "/", "/login" );
    public static final List<String> SUPER_USER_ALLOWED_URIS = Arrays.asList("/v1/event" , "/v1/chiffreAffaire/journalier");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        boolean isAllowedURI = isAllowedURI(requestURI);

        if (!isAllowedURI) {

            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                redirectToLogin(request, response);
                return;
            }
            Utilisateur utilisateur = (Utilisateur) session.getAttribute("user");
            System.out.println(utilisateur.getProfil());
            if (isSuperUser(requestURI) && !isSuperUser(utilisateur)) {
                response.sendRedirect(request.getContextPath() + SIMPLE_USER_PAGE);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAllowedURI(String requestURI) {
        return ALLOWED_URIS.contains(requestURI);

    }

    private boolean isSuperUser(String requestURI) {
        return SUPER_USER_ALLOWED_URIS.contains(requestURI);
    }

    private boolean isSuperUser(Utilisateur utilisateur) {
        return utilisateur.getProfil()== Profil.SUPERUSER;
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + LOGIN_URL);
    }

}

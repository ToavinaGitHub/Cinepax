package com.cinepax.mg.security;

import jakarta.servlet.RequestDispatcher;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        int error = 404;

        if (accessDeniedException instanceof AccessDeniedException) {
            error = 403; // Gérer l'accès refusé
        }


        response.sendRedirect(request.getContextPath()+"/error/"+error);

    }
}

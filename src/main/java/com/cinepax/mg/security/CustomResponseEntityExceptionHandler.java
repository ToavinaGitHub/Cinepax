package com.cinepax.mg.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler {

    @ExceptionHandler(value = { NoResourceFoundException.class })
    public ResponseEntity<Object> noHandlerFoundException(Exception ex) {
        // Rediriger vers une URL spécifique pour la page d'erreur 404
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/error/404").body(null);
    }
}

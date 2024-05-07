package com.cinepax.mg.security;

import com.cinepax.mg.Model.Profil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(
                        auth -> {
                            auth.requestMatchers("/" ,"/login" ,"/error/403" ,"/vendor/**" ,"/css/**","/img/**","/jquery/**","/js/**","/parsley/**","/scss/**").permitAll();
                            auth.requestMatchers("/v1/venteBillet/statParFilm" ).hasRole("SUPERUSER");

                            /*auth.requestMatchers("/rh").hasRole("RH");
                            auth.requestMatchers("/service").hasRole("SERVICE");
                            auth.requestMatchers("/user").hasRole("USER");*/
                            auth.anyRequest().authenticated();
                            //auth.anyRequest().permitAll();
                        }
                )
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()

                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }
}
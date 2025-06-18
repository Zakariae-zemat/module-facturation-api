package com.arimayi.facturation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de la sécurité Spring Security
 * - Authentification HTTP Basic avec utilisateur en mémoire
 * - Swagger public, reste des endpoints protégés
 */
@Configuration
public class SecurityConfig {

    /**
     * Déclare un utilisateur en mémoire (admin/password)
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User
                .withUsername("admin")
                .password("{noop}password") // {noop} = mot de passe en clair pour dev/test uniquement
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * Configuration de la chaîne de filtres de sécurité HTTP
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable() // désactive CSRF pour les appels API (non-navigateur)
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                    ).permitAll() // Swagger public
                    .anyRequest().authenticated() // le reste nécessite auth
                )
                .httpBasic() // active HTTP Basic Auth
                .and()
                .build();
    }
}

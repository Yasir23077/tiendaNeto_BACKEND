package com.universidad.servicioo.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    CookieCsrfTokenRepository csrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
            CookieCsrfTokenRepository csrfRepository,
            SecurityContextRepository securityContextRepository) throws Exception {
        http
            .csrf(csrf -> csrf.csrfTokenRepository(csrfRepository))
            .securityContext(context -> context.securityContextRepository(securityContextRepository))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .exceptionHandling(errors -> errors
                .authenticationEntryPoint((request, response, exception) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write("{\"mensaje\":\"Inicia sesión para continuar.\"}");
                })
                .accessDeniedHandler((request, response, exception) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write("{\"mensaje\":\"No tienes permisos para realizar esta acción.\"}");
                }))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/auth/csrf").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/ventas", "/api/ventas/**").hasAnyRole("CAJERO", "LIDER", "ENCARGADO")
                .requestMatchers(HttpMethod.POST, "/api/ventas").hasAnyRole("CAJERO", "LIDER", "ENCARGADO")
                .requestMatchers(HttpMethod.PUT, "/api/ventas/**").hasAnyRole("LIDER", "ENCARGADO")
                .requestMatchers(HttpMethod.DELETE, "/api/ventas/**").hasRole("ENCARGADO")
                .requestMatchers(HttpMethod.GET, "/api/productos", "/api/sucursales").hasAnyRole("CAJERO", "LIDER", "ENCARGADO")
                .requestMatchers(HttpMethod.POST, "/api/productos", "/api/sucursales").hasRole("ENCARGADO")
                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("ENCARGADO")
                .requestMatchers(HttpMethod.GET, "/api/auditoria").hasRole("ENCARGADO")
                .anyRequest().denyAll());
        return http.build();
    }
}

package com.universidad.servicioo.controller;

import com.universidad.servicioo.dto.request.LoginRequest;
import com.universidad.servicioo.dto.response.UsuarioResponse;
import com.universidad.servicioo.repository.UsuarioSistemaRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final UsuarioSistemaRepository usuarios;

    public AuthController(AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository,
            UsuarioSistemaRepository usuarios) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
        this.usuarios = usuarios;
    }

    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken csrfToken) {
        return Map.of("headerName", csrfToken.getHeaderName(), "token", csrfToken.getToken());
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            request.username().trim(), request.password()));
            if (httpRequest.getSession(false) != null) httpRequest.changeSessionId();
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, httpRequest, httpResponse);
            var usuario = usuarios.findByUsernameIgnoreCase(authentication.getName())
                    .orElseThrow(() -> new AuthenticationServiceException("No se encontró el perfil."));
            return ResponseEntity.ok(UsuarioResponse.desde(usuario));
        } catch (AuthenticationException exception) {
            SecurityContextHolder.clearContext();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "El usuario o la contraseña no son correctos."));
        }
    }

    @GetMapping("/me")
    public UsuarioResponse sesionActual(Authentication authentication) {
        var usuario = usuarios.findByUsernameIgnoreCase(authentication.getName())
                .orElseThrow(() -> new AuthenticationServiceException("No se encontró el perfil."));
        return UsuarioResponse.desde(usuario);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> cerrarSesion(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        var sesion = request.getSession(false);
        if (sesion != null) sesion.invalidate();
        return ResponseEntity.noContent().build();
    }
}

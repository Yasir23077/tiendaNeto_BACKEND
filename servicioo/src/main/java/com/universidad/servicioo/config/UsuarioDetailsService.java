package com.universidad.servicioo.config;

import com.universidad.servicioo.repository.UsuarioSistemaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    private final UsuarioSistemaRepository usuarios;

    public UsuarioDetailsService(UsuarioSistemaRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = usuarios.findByUsernameIgnoreCase(username)
                .filter(u -> u.isActivo())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario o contraseña incorrectos."));
        return User.withUsername(usuario.getUsername())
                .password(usuario.getPasswordHash())
                .authorities("ROLE_" + usuario.getRol())
                .build();
    }
}

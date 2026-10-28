package com.universidad.servicioo.config;

import com.universidad.servicioo.model.UsuarioSistema;
import com.universidad.servicioo.repository.UsuarioSistemaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuariosIniciales implements CommandLineRunner {
    private final UsuarioSistemaRepository usuarios;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.security.demo-users.enabled:true}")
    private boolean habilitarUsuariosDemo;
    @Value("${app.security.demo-users.cajero-password}")
    private String passwordCajero;
    @Value("${app.security.demo-users.lider-password}")
    private String passwordLider;
    @Value("${app.security.demo-users.encargado-password}")
    private String passwordEncargado;

    public UsuariosIniciales(UsuarioSistemaRepository usuarios, PasswordEncoder passwordEncoder) {
        this.usuarios = usuarios;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!habilitarUsuariosDemo) return;
        crearSiNoExiste("cajero", passwordCajero, "Alex Rivera", "CAJERO");
        crearSiNoExiste("lider", passwordLider, "Mariana Torres", "LIDER");
        crearSiNoExiste("encargado", passwordEncargado, "Daniel Morales", "ENCARGADO");
    }

    private void crearSiNoExiste(String username, String password, String nombre, String rol) {
        if (usuarios.existsByUsernameIgnoreCase(username)) return;
        usuarios.save(new UsuarioSistema(username, passwordEncoder.encode(password), nombre, rol));
    }
}

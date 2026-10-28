package com.universidad.servicioo.repository;

import com.universidad.servicioo.model.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioSistemaRepository extends JpaRepository<UsuarioSistema, Long> {
    Optional<UsuarioSistema> findByUsernameIgnoreCase(String username);
    boolean existsByUsernameIgnoreCase(String username);
}

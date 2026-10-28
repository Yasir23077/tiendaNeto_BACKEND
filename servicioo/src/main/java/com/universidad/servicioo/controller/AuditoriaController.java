package com.universidad.servicioo.controller;

import com.universidad.servicioo.dto.response.AuditoriaResponse;
import com.universidad.servicioo.repository.AuditoriaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {
    private final AuditoriaRepository repo;

    public AuditoriaController(AuditoriaRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    @PreAuthorize("hasRole('ENCARGADO')")
    public ResponseEntity<?> listar() {
        List<AuditoriaResponse> registros = repo.findTop500ByOrderByFechaDescIdDesc()
                .stream().map(AuditoriaResponse::desde).toList();
        return ResponseEntity.ok(registros);
    }
}

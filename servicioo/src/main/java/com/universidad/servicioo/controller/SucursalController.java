package com.universidad.servicioo.controller;

import com.universidad.servicioo.dto.request.SucursalRequest;
import com.universidad.servicioo.dto.response.SucursalResponse;
import com.universidad.servicioo.model.Sucursal;
import com.universidad.servicioo.repository.SucursalRepository;
import com.universidad.servicioo.service.AuditoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {
    private final SucursalRepository repo;
    private final AuditoriaService auditoria;

    public SucursalController(SucursalRepository repo, AuditoriaService auditoria) {
        this.repo = repo;
        this.auditoria = auditoria;
    }

    @GetMapping
    public List<SucursalResponse> listar() {
        return repo.findByActivaTrueOrderByNombreAsc().stream().map(SucursalResponse::desde).toList();
    }

    @PostMapping
    @Transactional
    @PreAuthorize("hasRole('ENCARGADO')")
    public ResponseEntity<?> crear(@Valid @RequestBody SucursalRequest request,
            @AuthenticationPrincipal UserDetails actor) {
        String codigo = request.codigo().trim();
        if (repo.existsByCodigoIgnoreCase(codigo)) {
            return ResponseEntity.badRequest().body("Ya existe una sucursal con ese código.");
        }
        Sucursal sucursal = new Sucursal();
        sucursal.setCodigo(codigo);
        sucursal.setNombre(request.nombre().trim());
        sucursal.setActiva(true);
        Sucursal guardada = repo.save(sucursal);
        auditoria.registrar(actor.getUsername(), "encargado", "CREAR", "Sucursal", guardada.getId(),
                guardada.getCodigo() + " · " + guardada.getNombre(), guardada.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(SucursalResponse.desde(guardada));
    }
}

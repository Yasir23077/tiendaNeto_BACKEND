package com.universidad.servicioo.controller;

import com.universidad.servicioo.dto.request.ProductoRequest;
import com.universidad.servicioo.dto.response.ProductoResponse;
import com.universidad.servicioo.model.Producto;
import com.universidad.servicioo.repository.ProductoRepository;
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
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoRepository repo;
    private final AuditoriaService auditoria;

    public ProductoController(ProductoRepository repo, AuditoriaService auditoria) {
        this.repo = repo;
        this.auditoria = auditoria;
    }

    @GetMapping
    public List<ProductoResponse> listar() {
        return repo.findByActivoTrueOrderByNombreAsc().stream().map(ProductoResponse::desde).toList();
    }

    @PostMapping
    @Transactional
    @PreAuthorize("hasRole('ENCARGADO')")
    public ResponseEntity<?> crear(@Valid @RequestBody ProductoRequest request,
            @AuthenticationPrincipal UserDetails actor) {
        String codigo = request.codigo().trim();
        if (repo.existsByCodigoIgnoreCase(codigo)) {
            return ResponseEntity.badRequest().body("Ya existe un producto con ese código.");
        }
        Producto producto = new Producto();
        producto.setCodigo(codigo);
        producto.setNombre(request.nombre().trim());
        producto.setCategoria(request.categoria() == null || request.categoria().isBlank() ? "General" : request.categoria().trim());
        producto.setPrecio(request.precio());
        producto.setActivo(true);
        Producto guardado = repo.save(producto);
        auditoria.registrar(actor.getUsername(), "encargado", "CREAR", "Producto", guardado.getId(),
                guardado.getCodigo() + " · " + guardado.getNombre() + " · $" + guardado.getPrecio(), null);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductoResponse.desde(guardado));
    }

    @PutMapping("/{id}")
    @Transactional
    @PreAuthorize("hasRole('ENCARGADO')")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request,
            @AuthenticationPrincipal UserDetails actor) {
        String codigo = request.codigo().trim();
        if (repo.existsByCodigoIgnoreCaseAndIdNot(codigo, id)) {
            return ResponseEntity.badRequest().body("Ya existe un producto con ese código.");
        }
        var encontrada = repo.findById(id);
        if (encontrada.isEmpty()) return ResponseEntity.notFound().build();
        Producto producto = encontrada.get();
        producto.setCodigo(codigo);
        producto.setNombre(request.nombre().trim());
        producto.setCategoria(request.categoria() == null || request.categoria().isBlank() ? "General" : request.categoria().trim());
        producto.setPrecio(request.precio());
        Producto guardado = repo.save(producto);
        auditoria.registrar(actor.getUsername(), "encargado", "ACTUALIZAR", "Producto", guardado.getId(),
                guardado.getCodigo() + " · " + guardado.getNombre() + " · $" + guardado.getPrecio(), null);
        return ResponseEntity.ok(ProductoResponse.desde(guardado));
    }
}

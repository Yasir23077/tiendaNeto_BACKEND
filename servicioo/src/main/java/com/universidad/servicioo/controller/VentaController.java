package com.universidad.servicioo.controller;

import com.universidad.servicioo.dto.request.VentaRequest;
import com.universidad.servicioo.dto.response.VentaResponse;
import com.universidad.servicioo.model.Producto;
import com.universidad.servicioo.model.Venta;
import com.universidad.servicioo.repository.ProductoRepository;
import com.universidad.servicioo.repository.VentaRepository;
import com.universidad.servicioo.service.AuditoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    private final VentaRepository repo;
    private final ProductoRepository productos;
    private final AuditoriaService auditoria;

    public VentaController(VentaRepository repo, ProductoRepository productos, AuditoriaService auditoria) {
        this.repo = repo;
        this.productos = productos;
        this.auditoria = auditoria;
    }

    @GetMapping
    public List<VentaResponse> listar() {
        return repo.findAll().stream().map(VentaResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> obtener(@PathVariable Long id) {
        return repo.findById(id).map(VentaResponse::desde).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    @PreAuthorize("hasAnyRole('CAJERO', 'LIDER', 'ENCARGADO')")
    public ResponseEntity<VentaResponse> crear(@Valid @RequestBody VentaRequest request,
            @AuthenticationPrincipal UserDetails actor) {
        Venta venta = new Venta();
        venta.setProducto(request.producto().trim());
        venta.setCantidad(request.cantidad());
        venta.setSucursal(request.sucursal().trim());
        venta.setPrecioUnitario(request.precioUnitario());
        productos.findFirstByNombreIgnoreCaseAndActivoTrue(venta.getProducto())
                .ifPresent(producto -> venta.setPrecioUnitario(producto.getPrecio()));
        venta.setTotal(venta.getPrecioUnitario().multiply(BigDecimal.valueOf(venta.getCantidad())));
        Venta guardada = repo.save(venta);
        auditoria.registrar(actor.getUsername(), rol(actor), "CREAR", "Venta", guardada.getId(),
                guardada.getProducto() + " · " + guardada.getCantidad() + " pza. · $" + guardada.getTotal(), guardada.getSucursal());
        return ResponseEntity.status(HttpStatus.CREATED).body(VentaResponse.desde(guardada));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<VentaResponse> actualizar(@PathVariable Long id, @Valid @RequestBody VentaRequest request,
            @AuthenticationPrincipal UserDetails actor) {
        var encontrada = repo.findById(id);
        if (encontrada.isEmpty()) return ResponseEntity.notFound().build();
        Venta venta = encontrada.get();
        venta.setProducto(request.producto().trim());
        venta.setCantidad(request.cantidad());
        venta.setPrecioUnitario(productos.findFirstByNombreIgnoreCaseAndActivoTrue(venta.getProducto())
                .map(Producto::getPrecio).orElse(request.precioUnitario()));
        venta.setSucursal(request.sucursal().trim());
        venta.setTotal(venta.getPrecioUnitario().multiply(BigDecimal.valueOf(venta.getCantidad())));
        Venta guardada = repo.save(venta);
        auditoria.registrar(actor.getUsername(), rol(actor), "ACTUALIZAR", "Venta", guardada.getId(),
                guardada.getProducto() + " · " + guardada.getCantidad() + " pza. · $" + guardada.getTotal(), guardada.getSucursal());
        return ResponseEntity.ok(VentaResponse.desde(guardada));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminar(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails actor) {
        var encontrada = repo.findById(id);
        if (encontrada.isEmpty()) return ResponseEntity.notFound().build();
        Venta venta = encontrada.get();
        repo.deleteById(id);
        auditoria.registrar(actor.getUsername(), rol(actor), "ELIMINAR", "Venta", id,
                venta.getProducto() + " · " + venta.getCantidad() + " pza. · $" + venta.getTotal(), venta.getSucursal());
        return ResponseEntity.noContent().build();
    }

    private String rol(UserDetails actor) {
        return actor.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "").toLowerCase();
    }
}

// Archivo: src/main/java/com/tiendasneto/ventas/controlador/ControladorVenta.java. Funcion: Atiende las solicitudes para consultar y administrar ventas.
// Declara el paquete com.tiendasneto.ventas.controlador al que pertenece esta clase.
package com.tiendasneto.ventas.controlador;

// Importa com.tiendasneto.ventas.transferencia.solicitud.SolicitudVenta para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.transferencia.solicitud.SolicitudVenta;
// Importa com.tiendasneto.ventas.transferencia.solicitud.SolicitudVentaMultiple para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.transferencia.solicitud.SolicitudVentaMultiple;
// Importa com.tiendasneto.ventas.transferencia.respuesta.RespuestaVenta para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.transferencia.respuesta.RespuestaVenta;
// Importa com.tiendasneto.ventas.modelo.Producto para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.Producto;
// Importa com.tiendasneto.ventas.modelo.Venta para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.Venta;
// Importa com.tiendasneto.ventas.repositorio.RepositorioProducto para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.repositorio.RepositorioProducto;
// Importa com.tiendasneto.ventas.repositorio.RepositorioVenta para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.repositorio.RepositorioVenta;
// Importa com.tiendasneto.ventas.servicio.ServicioAuditoria para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.servicio.ServicioAuditoria;
// Importa jakarta.validation.Valid para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.Valid;
// Importa org.springframework.http.HttpStatus para poder usar ese tipo o paquete en este archivo.
import org.springframework.http.HttpStatus;
// Importa org.springframework.http.ResponseEntity para poder usar ese tipo o paquete en este archivo.
import org.springframework.http.ResponseEntity;
// Importa org.springframework.web.server.ResponseStatusException para poder usar ese tipo o paquete en este archivo.
import org.springframework.web.server.ResponseStatusException;
// Importa org.springframework.transaction.annotation.Transactional para poder usar ese tipo o paquete en este archivo.
import org.springframework.transaction.annotation.Transactional;
// Importa org.springframework.security.access.prepost.PreAuthorize para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.access.prepost.PreAuthorize;
// Importa org.springframework.security.core.userdetails.UserDetails para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.core.userdetails.UserDetails;
// Importa org.springframework.security.core.annotation.AuthenticationPrincipal para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.core.annotation.AuthenticationPrincipal;
// Importa org.springframework.web.bind.annotation.* para poder usar ese tipo o paquete en este archivo.
import org.springframework.web.bind.annotation.*;

// Importa java.math.BigDecimal para poder usar ese tipo o paquete en este archivo.
import java.math.BigDecimal;
// Importa java.time.LocalDateTime para poder usar ese tipo o paquete en este archivo.
import java.time.LocalDateTime;
// Importa java.util.ArrayList para poder usar ese tipo o paquete en este archivo.
import java.util.ArrayList;
// Importa java.util.List para poder usar ese tipo o paquete en este archivo.
import java.util.List;
// Importa java.util.UUID para poder usar ese tipo o paquete en este archivo.
import java.util.UUID;

// Aplica la anotación @RestController a la declaración que sigue.
@RestController
// Aplica la anotación @RequestMapping a la declaración que sigue.
@RequestMapping("/api/ventas")
// Declara class ControladorVenta y define su bloque de implementación.
public class ControladorVenta {
    // Declara un atributo o constante disponible en el ámbito indicado.
    private final RepositorioVenta repo;
    // Declara un atributo o constante disponible en el ámbito indicado.
    private final RepositorioProducto productos;
    // Declara un atributo o constante disponible en el ámbito indicado.
    private final ServicioAuditoria auditoria;

    // Función: Constructor: crea un objeto ControladorVenta con las dependencias que necesita.
    public ControladorVenta(RepositorioVenta repo, RepositorioProducto productos, ServicioAuditoria auditoria) {
        // Asigna o consulta el atributo de esta instancia.
        this.repo = repo;
        // Asigna o consulta el atributo de esta instancia.
        this.productos = productos;
        // Asigna o consulta el atributo de esta instancia.
        this.auditoria = auditoria;
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Aplica la anotación @GetMapping a la declaración que sigue.
    @GetMapping
    // Función: Consulta los datos de esta sección, los convierte a respuestas y los devuelve.
    public List<RespuestaVenta> listar() {
        // Devuelve el resultado calculado por este método o bloque.
        return repo.findAll().stream().map(RespuestaVenta::desde).toList();
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Aplica la anotación @GetMapping a la declaración que sigue.
    @GetMapping("/{id}")
    // Función: Busca el registro indicado y devuelve 404 si no existe.
    public ResponseEntity<RespuestaVenta> obtener(@PathVariable Long id) {
        // Devuelve el resultado calculado por este método o bloque.
        return repo.findById(id).map(RespuestaVenta::desde).map(ResponseEntity::ok)
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .orElse(ResponseEntity.notFound().build());
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Aplica la anotación @PostMapping a la declaración que sigue.
    @PostMapping
    // Aplica la anotación @Transactional a la declaración que sigue.
    @Transactional
    // Los tres roles pueden registrar una venta nueva.
    @PreAuthorize("hasAnyRole('CAJERO', 'LIDER', 'ENCARGADO')")
    // Función: Valida los datos, guarda el registro y devuelve el resultado.
    public ResponseEntity<RespuestaVenta> crear(@Valid @RequestBody SolicitudVenta request,
            // Aplica la anotación @AuthenticationPrincipal a la declaración que sigue.
            @AuthenticationPrincipal UserDetails actor) {
        // Declara un valor local usado por la lógica de este bloque.
        Venta venta = new Venta();
        // Ejecuta esta instrucción de la lógica actual.
        venta.setProducto(request.producto().trim());
        // Ejecuta esta instrucción de la lógica actual.
        venta.setCantidad(request.cantidad());
        // Ejecuta esta instrucción de la lógica actual.
        venta.setSucursal(request.sucursal().trim());
        // Ejecuta esta instrucción de la lógica actual.
        venta.setPrecioUnitario(request.precioUnitario());
        // Continúa la expresión o declaración iniciada en las líneas cercanas.
        productos.findFirstByNombreIgnoreCaseAndActivoTrue(venta.getProducto())
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .ifPresent(producto -> venta.setPrecioUnitario(producto.getPrecio()));
        // Ejecuta esta instrucción de la lógica actual.
        venta.setTotal(venta.getPrecioUnitario().multiply(BigDecimal.valueOf(venta.getCantidad())));
        // Declara un valor local usado por la lógica de este bloque.
        Venta guardada = repo.save(venta);
        // Continúa la expresión o declaración iniciada en las líneas cercanas.
        auditoria.registrar(actor.getUsername(), rol(actor), "CREAR", "Venta", guardada.getId(),
                // Ejecuta esta instrucción de la lógica actual.
                guardada.getProducto() + " · " + guardada.getCantidad() + " pza. · $" + guardada.getTotal(), guardada.getSucursal());
        // Devuelve el resultado calculado por este método o bloque.
        return ResponseEntity.status(HttpStatus.CREATED).body(RespuestaVenta.desde(guardada));
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Registra todos los productos de un cliente bajo un folio y una fecha comunes.
    @PostMapping("/lote")
    // Aplica la anotación @Transactional a la declaración que sigue.
    @Transactional
    // Aplica la anotación @PreAuthorize a la declaración que sigue.
    @PreAuthorize("hasAnyRole('CAJERO', 'LIDER', 'ENCARGADO')")
    // Función: Guarda todas las líneas del carrito con folio común dentro de una transacción.
    public ResponseEntity<List<RespuestaVenta>> crearLote(@Valid @RequestBody SolicitudVentaMultiple request,
            // Aplica la anotación @AuthenticationPrincipal a la declaración que sigue.
            @AuthenticationPrincipal UserDetails actor) {
        // Declara un valor local usado por la lógica de este bloque.
        String folio = "V-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        // Declara un valor local usado por la lógica de este bloque.
        String sucursal = request.sucursal().trim();
        // Ejecuta esta instrucción de la lógica actual.
        LocalDateTime fecha = LocalDateTime.now();
        // Declara un valor local usado por la lógica de este bloque.
        List<Venta> detalle = new ArrayList<>();

        // Controla el flujo de ejecución según esta condición o situación.
        for (SolicitudVentaMultiple.Detalle item : request.productos()) {
            // Declara un valor local usado por la lógica de este bloque.
            Producto producto = productos.findById(item.productoId())
                    // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                    .filter(Producto::isActivo)
                    // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            // Ejecuta esta instrucción de la lógica actual.
                            "Uno de los productos ya no esta disponible."));
            // Declara un valor local usado por la lógica de este bloque.
            Venta linea = new Venta();
            // Ejecuta esta instrucción de la lógica actual.
            linea.setFolio(folio);
            // Ejecuta esta instrucción de la lógica actual.
            linea.setFecha(fecha);
            // Ejecuta esta instrucción de la lógica actual.
            linea.setProducto(producto.getNombre());
            // Ejecuta esta instrucción de la lógica actual.
            linea.setCantidad(item.cantidad());
            // Ejecuta esta instrucción de la lógica actual.
            linea.setPrecioUnitario(producto.getPrecio());
            // Ejecuta esta instrucción de la lógica actual.
            linea.setTotal(producto.getPrecio().multiply(BigDecimal.valueOf(item.cantidad())));
            // Ejecuta esta instrucción de la lógica actual.
            linea.setSucursal(sucursal);
            // Ejecuta esta instrucción de la lógica actual.
            detalle.add(linea);
        // Cierra el bloque o la expresión que se definió anteriormente.
        }

        // Declara un valor local usado por la lógica de este bloque.
        List<Venta> guardadas = repo.saveAll(detalle);
        // Ejecuta esta instrucción de la lógica actual.
        repo.flush();
        // Ejecuta esta instrucción de la lógica actual.
        BigDecimal total = guardadas.stream().map(Venta::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        // Declara un valor local usado por la lógica de este bloque.
        String resumen = folio + " · " + guardadas.size() + " productos · $" + total;
        // Ejecuta esta instrucción de la lógica actual.
        auditoria.registrar(actor.getUsername(), rol(actor), "CREAR", "Venta", guardadas.get(0).getId(), resumen, sucursal);
        // Devuelve el resultado calculado por este método o bloque.
        return ResponseEntity.status(HttpStatus.CREATED).body(guardadas.stream().map(RespuestaVenta::desde).toList());
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Aplica la anotación @PutMapping a la declaración que sigue.
    @PutMapping("/{id}")
    // Aplica la anotación @Transactional a la declaración que sigue.
    @Transactional
    // Aplica la anotación @PreAuthorize a la declaración que sigue.
    @PreAuthorize("hasAnyRole('LIDER', 'ENCARGADO')")
    // Función: Busca el registro y guarda los cambios permitidos.
    public ResponseEntity<RespuestaVenta> actualizar(@PathVariable Long id, @Valid @RequestBody SolicitudVenta request,
            // Aplica la anotación @AuthenticationPrincipal a la declaración que sigue.
            @AuthenticationPrincipal UserDetails actor) {
        // Declara un valor local usado por la lógica de este bloque.
        var encontrada = repo.findById(id);
        // Controla el flujo de ejecución según esta condición o situación.
        if (encontrada.isEmpty()) return ResponseEntity.notFound().build();
        // Declara un valor local usado por la lógica de este bloque.
        Venta venta = encontrada.get();
        // Ejecuta esta instrucción de la lógica actual.
        venta.setProducto(request.producto().trim());
        // Ejecuta esta instrucción de la lógica actual.
        venta.setCantidad(request.cantidad());
        // Continúa la expresión o declaración iniciada en las líneas cercanas.
        venta.setPrecioUnitario(productos.findFirstByNombreIgnoreCaseAndActivoTrue(venta.getProducto())
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .map(Producto::getPrecio).orElse(request.precioUnitario()));
        // Ejecuta esta instrucción de la lógica actual.
        venta.setSucursal(request.sucursal().trim());
        // Ejecuta esta instrucción de la lógica actual.
        venta.setTotal(venta.getPrecioUnitario().multiply(BigDecimal.valueOf(venta.getCantidad())));
        // Declara un valor local usado por la lógica de este bloque.
        Venta guardada = repo.save(venta);
        // Continúa la expresión o declaración iniciada en las líneas cercanas.
        auditoria.registrar(actor.getUsername(), rol(actor), "ACTUALIZAR", "Venta", guardada.getId(),
                // Ejecuta esta instrucción de la lógica actual.
                guardada.getProducto() + " · " + guardada.getCantidad() + " pza. · $" + guardada.getTotal(), guardada.getSucursal());
        // Devuelve el resultado calculado por este método o bloque.
        return ResponseEntity.ok(RespuestaVenta.desde(guardada));
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Aplica la anotación @DeleteMapping a la declaración que sigue.
    @DeleteMapping("/{id}")
    // Aplica la anotación @Transactional a la declaración que sigue.
    @Transactional
    // Aplica la anotación @PreAuthorize a la declaración que sigue.
    @PreAuthorize("hasAnyRole('LIDER', 'ENCARGADO')")
    // Función: Elimina el registro seleccionado y registra la acción.
    public ResponseEntity<Void> eliminar(@PathVariable Long id,
            // Aplica la anotación @AuthenticationPrincipal a la declaración que sigue.
            @AuthenticationPrincipal UserDetails actor) {
        // Declara un valor local usado por la lógica de este bloque.
        var encontrada = repo.findById(id);
        // Controla el flujo de ejecución según esta condición o situación.
        if (encontrada.isEmpty()) return ResponseEntity.notFound().build();
        // Declara un valor local usado por la lógica de este bloque.
        Venta venta = encontrada.get();
        // Ejecuta esta instrucción de la lógica actual.
        repo.deleteById(id);
        // Continúa la expresión o declaración iniciada en las líneas cercanas.
        auditoria.registrar(actor.getUsername(), rol(actor), "ELIMINAR", "Venta", id,
                // Ejecuta esta instrucción de la lógica actual.
                venta.getProducto() + " · " + venta.getCantidad() + " pza. · $" + venta.getTotal(), venta.getSucursal());
        // Devuelve el resultado calculado por este método o bloque.
        return ResponseEntity.noContent().build();
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Función: Normaliza el rol del usuario para guardarlo en auditoría.
    private String rol(UserDetails actor) {
        // Devuelve el resultado calculado por este método o bloque.
        return actor.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "").toLowerCase();
    // Cierra el bloque o la expresión que se definió anteriormente.
    }
// Cierra el bloque o la expresión que se definió anteriormente.
}

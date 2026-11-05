// Archivo: src/main/java/com/tiendasneto/ventas/controlador/ControladorProducto.java. Funcion: Atiende las solicitudes de consulta y administracion de productos.
// Declara el paquete com.tiendasneto.ventas.controlador al que pertenece esta clase.
package com.tiendasneto.ventas.controlador;

// Importa com.tiendasneto.ventas.transferencia.solicitud.SolicitudProducto para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.transferencia.solicitud.SolicitudProducto;
// Importa com.tiendasneto.ventas.transferencia.respuesta.RespuestaProducto para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.transferencia.respuesta.RespuestaProducto;
// Importa com.tiendasneto.ventas.modelo.Producto para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.Producto;
// Importa com.tiendasneto.ventas.repositorio.RepositorioProducto para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.repositorio.RepositorioProducto;
// Importa com.tiendasneto.ventas.servicio.ServicioAuditoria para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.servicio.ServicioAuditoria;
// Importa jakarta.validation.Valid para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.Valid;
// Importa org.springframework.http.HttpStatus para poder usar ese tipo o paquete en este archivo.
// Importa org.springframework.http.ResponseEntity para poder usar ese tipo o paquete en este archivo.
import org.springframework.http.ResponseEntity;
// Importa org.springframework.transaction.annotation.Transactional para poder usar ese tipo o paquete en este archivo.
import org.springframework.transaction.annotation.Transactional;
// Importa org.springframework.security.access.prepost.PreAuthorize para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.access.prepost.PreAuthorize;
// Importa org.springframework.security.core.annotation.AuthenticationPrincipal para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.core.annotation.AuthenticationPrincipal;
// Importa org.springframework.security.core.userdetails.UserDetails para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.core.userdetails.UserDetails;
// Importa org.springframework.web.bind.annotation.* para poder usar ese tipo o paquete en este archivo.
import org.springframework.web.bind.annotation.*;
// Construye la cabecera Location con la URL del producto recién creado.
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

// Importa java.util.List para poder usar ese tipo o paquete en este archivo.
import java.util.List;
// Usa mapas para adjuntar errores de validación a los nombres de campo.
import java.util.Map;
// Representa la URL absoluta del nuevo recurso.
import java.net.URI;

// Aplica la anotación @RestController a la declaración que sigue.
@RestController
// Aplica la anotación @RequestMapping a la declaración que sigue.
@RequestMapping("/api/productos")
// Declara class ControladorProducto y define su bloque de implementación.
public class ControladorProducto {
    // Declara un atributo o constante disponible en el ámbito indicado.
    private final RepositorioProducto repo;
    // Declara un atributo o constante disponible en el ámbito indicado.
    private final ServicioAuditoria auditoria;

    // Función: Constructor: crea un objeto ControladorProducto con las dependencias que necesita.
    public ControladorProducto(RepositorioProducto repo, ServicioAuditoria auditoria) {
        // Asigna o consulta el atributo de esta instancia.
        this.repo = repo;
        // Asigna o consulta el atributo de esta instancia.
        this.auditoria = auditoria;
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Devuelve un producto activo por ID o HTTP 404 cuando no existe.
    @GetMapping("/{id}")
    public ResponseEntity<RespuestaProducto> obtener(@PathVariable Long id) {
        var encontrado = repo.findById(id).filter(Producto::isActivo);
        if (encontrado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(RespuestaProducto.desde(encontrado.get()));
    }

    // Aplica la anotación @GetMapping a la declaración que sigue.
    @GetMapping
    // Función: Consulta los datos de esta sección, los convierte a respuestas y los devuelve.
    public List<RespuestaProducto> listar() {
        // Devuelve el resultado calculado por este método o bloque.
        return repo.findByActivoTrueOrderByNombreAsc().stream().map(RespuestaProducto::desde).toList();
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Aplica la anotación @PostMapping a la declaración que sigue.
    @PostMapping
    // Aplica la anotación @Transactional a la declaración que sigue.
    @Transactional
    // Cajeros consultan; solo lider y encargado crean productos.
    @PreAuthorize("hasAnyRole('LIDER', 'ENCARGADO')")
    // Función: Valida los datos, guarda el registro y devuelve el resultado.
    public ResponseEntity<?> crear(@Valid @RequestBody SolicitudProducto request,
            // Aplica la anotación @AuthenticationPrincipal a la declaración que sigue.
            @AuthenticationPrincipal UserDetails actor) {
        // Declara un valor local usado por la lógica de este bloque.
        String codigo = request.codigo().trim();
        // Controla el flujo de ejecución según esta condición o situación.
        if (repo.existsByCodigoIgnoreCase(codigo)) {
            // Devuelve el resultado calculado por este método o bloque.
            return ResponseEntity.badRequest().body(Map.of(
                    "mensaje", "Revisa los datos enviados.",
                    "errores", Map.of("codigo", "Ya existe un producto con ese código.")));
        // Cierra el bloque o la expresión que se definió anteriormente.
        }
        // Declara un valor local usado por la lógica de este bloque.
        Producto producto = new Producto();
        // Ejecuta esta instrucción de la lógica actual.
        producto.setCodigo(codigo);
        // Ejecuta esta instrucción de la lógica actual.
        producto.setNombre(request.nombre().trim());
        // Ejecuta esta instrucción de la lógica actual.
        producto.setCategoria(request.categoria() == null || request.categoria().isBlank() ? "General" : request.categoria().trim());
        // Ejecuta esta instrucción de la lógica actual.
        producto.setPrecio(request.precio());
        // Ejecuta esta instrucción de la lógica actual.
        producto.setActivo(true);
        // Declara un valor local usado por la lógica de este bloque.
        Producto guardado = repo.save(producto);
        // Continúa la expresión o declaración iniciada en las líneas cercanas.
        auditoria.registrar(actor.getUsername(), rol(actor), "CREAR", "Producto", guardado.getId(),
                // Ejecuta esta instrucción de la lógica actual.
                guardado.getCodigo() + " · " + guardado.getNombre() + " · $" + guardado.getPrecio(), null);
        // Devuelve el resultado calculado por este método o bloque.
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardado.getId())
                .toUri();
        return ResponseEntity.created(location).body(RespuestaProducto.desde(guardado));
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Aplica la anotación @PutMapping a la declaración que sigue.
    @PutMapping("/{id}")
    // Aplica la anotación @Transactional a la declaración que sigue.
    @Transactional
    // Solo lider y encargado actualizan productos y precios.
    @PreAuthorize("hasAnyRole('LIDER', 'ENCARGADO')")
    // Función: Busca el registro y guarda los cambios permitidos.
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody SolicitudProducto request,
            // Aplica la anotación @AuthenticationPrincipal a la declaración que sigue.
            @AuthenticationPrincipal UserDetails actor) {
        // Declara un valor local usado por la lógica de este bloque.
        String codigo = request.codigo().trim();
        // Controla el flujo de ejecución según esta condición o situación.
        if (repo.existsByCodigoIgnoreCaseAndIdNot(codigo, id)) {
            // Devuelve el resultado calculado por este método o bloque.
            return ResponseEntity.badRequest().body(Map.of(
                    "mensaje", "Revisa los datos enviados.",
                    "errores", Map.of("codigo", "Ya existe un producto con ese código.")));
        // Cierra el bloque o la expresión que se definió anteriormente.
        }
        // Declara un valor local usado por la lógica de este bloque.
        var encontrada = repo.findById(id);
        // Controla el flujo de ejecución según esta condición o situación.
        if (encontrada.isEmpty()) return ResponseEntity.notFound().build();
        // Declara un valor local usado por la lógica de este bloque.
        Producto producto = encontrada.get();
        // Ejecuta esta instrucción de la lógica actual.
        producto.setCodigo(codigo);
        // Ejecuta esta instrucción de la lógica actual.
        producto.setNombre(request.nombre().trim());
        // Ejecuta esta instrucción de la lógica actual.
        producto.setCategoria(request.categoria() == null || request.categoria().isBlank() ? "General" : request.categoria().trim());
        // Ejecuta esta instrucción de la lógica actual.
        producto.setPrecio(request.precio());
        // Declara un valor local usado por la lógica de este bloque.
        Producto guardado = repo.save(producto);
        // Continúa la expresión o declaración iniciada en las líneas cercanas.
        auditoria.registrar(actor.getUsername(), rol(actor), "ACTUALIZAR", "Producto", guardado.getId(),
                // Ejecuta esta instrucción de la lógica actual.
                guardado.getCodigo() + " · " + guardado.getNombre() + " · $" + guardado.getPrecio(), null);
        // Devuelve el resultado calculado por este método o bloque.
        return ResponseEntity.ok(RespuestaProducto.desde(guardado));
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Registra en auditoria el rol real de quien hizo el cambio.
    // Función: Normaliza el rol del usuario para guardarlo en auditoría.
    private String rol(UserDetails actor) {
        // Devuelve el resultado calculado por este método o bloque.
        return actor.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "").toLowerCase();
    // Cierra el bloque o la expresión que se definió anteriormente.
    }
// Cierra el bloque o la expresión que se definió anteriormente.
}

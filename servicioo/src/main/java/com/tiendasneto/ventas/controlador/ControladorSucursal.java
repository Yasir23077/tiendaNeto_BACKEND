// Archivo: src/main/java/com/tiendasneto/ventas/controlador/ControladorSucursal.java. Funcion: Atiende la consulta y el registro de sucursales.
// Declara el paquete com.tiendasneto.ventas.controlador al que pertenece esta clase.
package com.tiendasneto.ventas.controlador;

// Importa com.tiendasneto.ventas.transferencia.solicitud.SolicitudSucursal para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.transferencia.solicitud.SolicitudSucursal;
// Importa com.tiendasneto.ventas.transferencia.respuesta.RespuestaSucursal para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.transferencia.respuesta.RespuestaSucursal;
// Importa com.tiendasneto.ventas.modelo.Sucursal para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.Sucursal;
// Importa com.tiendasneto.ventas.repositorio.RepositorioSucursal para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.repositorio.RepositorioSucursal;
// Importa com.tiendasneto.ventas.servicio.ServicioAuditoria para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.servicio.ServicioAuditoria;
// Importa jakarta.validation.Valid para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.Valid;
// Importa org.springframework.http.HttpStatus para poder usar ese tipo o paquete en este archivo.
import org.springframework.http.HttpStatus;
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

// Importa java.util.List para poder usar ese tipo o paquete en este archivo.
import java.util.List;

// Aplica la anotación @RestController a la declaración que sigue.
@RestController
// Aplica la anotación @RequestMapping a la declaración que sigue.
@RequestMapping("/api/sucursales")
// Declara class ControladorSucursal y define su bloque de implementación.
public class ControladorSucursal {
    // Declara un atributo o constante disponible en el ámbito indicado.
    private final RepositorioSucursal repo;
    // Declara un atributo o constante disponible en el ámbito indicado.
    private final ServicioAuditoria auditoria;

    // Función: Constructor: crea un objeto ControladorSucursal con las dependencias que necesita.
    public ControladorSucursal(RepositorioSucursal repo, ServicioAuditoria auditoria) {
        // Asigna o consulta el atributo de esta instancia.
        this.repo = repo;
        // Asigna o consulta el atributo de esta instancia.
        this.auditoria = auditoria;
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Aplica la anotación @GetMapping a la declaración que sigue.
    @GetMapping
    // Función: Consulta los datos de esta sección, los convierte a respuestas y los devuelve.
    public List<RespuestaSucursal> listar() {
        // Devuelve el resultado calculado por este método o bloque.
        return repo.findByActivaTrueOrderByNombreAsc().stream().map(RespuestaSucursal::desde).toList();
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Aplica la anotación @PostMapping a la declaración que sigue.
    @PostMapping
    // Aplica la anotación @Transactional a la declaración que sigue.
    @Transactional
    // Cajeros consultan; solo lider y encargado registran sucursales.
    @PreAuthorize("hasAnyRole('LIDER', 'ENCARGADO')")
    // Función: Valida los datos, guarda el registro y devuelve el resultado.
    public ResponseEntity<?> crear(@Valid @RequestBody SolicitudSucursal request,
            // Aplica la anotación @AuthenticationPrincipal a la declaración que sigue.
            @AuthenticationPrincipal UserDetails actor) {
        // Declara un valor local usado por la lógica de este bloque.
        String codigo = request.codigo().trim();
        // Controla el flujo de ejecución según esta condición o situación.
        if (repo.existsByCodigoIgnoreCase(codigo)) {
            // Devuelve el resultado calculado por este método o bloque.
            return ResponseEntity.badRequest().body("Ya existe una sucursal con ese código.");
        // Cierra el bloque o la expresión que se definió anteriormente.
        }
        // Declara un valor local usado por la lógica de este bloque.
        Sucursal sucursal = new Sucursal();
        // Ejecuta esta instrucción de la lógica actual.
        sucursal.setCodigo(codigo);
        // Ejecuta esta instrucción de la lógica actual.
        sucursal.setNombre(request.nombre().trim());
        // Ejecuta esta instrucción de la lógica actual.
        sucursal.setActiva(true);
        // Declara un valor local usado por la lógica de este bloque.
        Sucursal guardada = repo.save(sucursal);
        // Continúa la expresión o declaración iniciada en las líneas cercanas.
        auditoria.registrar(actor.getUsername(), rol(actor), "CREAR", "Sucursal", guardada.getId(),
                // Ejecuta esta instrucción de la lógica actual.
                guardada.getCodigo() + " · " + guardada.getNombre(), guardada.getNombre());
        // Devuelve el resultado calculado por este método o bloque.
        return ResponseEntity.status(HttpStatus.CREATED).body(RespuestaSucursal.desde(guardada));
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

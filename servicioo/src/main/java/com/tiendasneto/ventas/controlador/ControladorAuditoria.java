// Archivo: src/main/java/com/tiendasneto/ventas/controlador/ControladorAuditoria.java. Funcion: Entrega la bitacora de acciones solo al encargado.
// Declara el paquete com.tiendasneto.ventas.controlador al que pertenece esta clase.
package com.tiendasneto.ventas.controlador;

// Importa com.tiendasneto.ventas.transferencia.respuesta.RespuestaAuditoria para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.transferencia.respuesta.RespuestaAuditoria;
// Importa com.tiendasneto.ventas.repositorio.RepositorioAuditoria para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.repositorio.RepositorioAuditoria;
// Importa org.springframework.http.ResponseEntity para poder usar ese tipo o paquete en este archivo.
import org.springframework.http.ResponseEntity;
// Importa org.springframework.security.access.prepost.PreAuthorize para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.access.prepost.PreAuthorize;
// Importa org.springframework.web.bind.annotation.* para poder usar ese tipo o paquete en este archivo.
import org.springframework.web.bind.annotation.*;

// Importa java.util.List para poder usar ese tipo o paquete en este archivo.
import java.util.List;

// Aplica la anotación @RestController a la declaración que sigue.
@RestController
// Aplica la anotación @RequestMapping a la declaración que sigue.
@RequestMapping("/api/auditoria")
// Declara class ControladorAuditoria y define su bloque de implementación.
public class ControladorAuditoria {
    // Declara un atributo o constante disponible en el ámbito indicado.
    private final RepositorioAuditoria repo;

    // Función: Constructor: crea un objeto ControladorAuditoria con las dependencias que necesita.
    public ControladorAuditoria(RepositorioAuditoria repo) {
        // Asigna o consulta el atributo de esta instancia.
        this.repo = repo;
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Aplica la anotación @GetMapping a la declaración que sigue.
    @GetMapping
    // Proteccion adicional por metodo: solo ENCARGADO puede consultar la auditoria.
    @PreAuthorize("hasRole('ENCARGADO')")
    // Función: Consulta los datos de esta sección, los convierte a respuestas y los devuelve.
    public ResponseEntity<?> listar() {
        // Declara un valor local usado por la lógica de este bloque.
        List<RespuestaAuditoria> registros = repo.findTop500ByOrderByFechaDescIdDesc()
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .stream().map(RespuestaAuditoria::desde).toList();
        // Devuelve el resultado calculado por este método o bloque.
        return ResponseEntity.ok(registros);
    // Cierra el bloque o la expresión que se definió anteriormente.
    }
// Cierra el bloque o la expresión que se definió anteriormente.
}

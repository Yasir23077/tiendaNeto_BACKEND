// Archivo: src/main/java/com/tiendasneto/ventas/controlador/ControladorAdministracionUsuarios.java. Funcion: Permite al encargado consultar, crear y editar cuentas de usuario.
// Declara el paquete com.tiendasneto.ventas.controlador al que pertenece esta clase.
package com.tiendasneto.ventas.controlador;

// Importa com.tiendasneto.ventas.transferencia.solicitud.SolicitudUsuario para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.transferencia.solicitud.SolicitudUsuario;
// Importa com.tiendasneto.ventas.transferencia.solicitud.SolicitudActualizacionUsuario para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.transferencia.solicitud.SolicitudActualizacionUsuario;
// Importa com.tiendasneto.ventas.transferencia.respuesta.RespuestaUsuarioAdministrador para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.transferencia.respuesta.RespuestaUsuarioAdministrador;
// Importa com.tiendasneto.ventas.modelo.UsuarioSistema para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.UsuarioSistema;
// Importa com.tiendasneto.ventas.repositorio.RepositorioUsuarioSistema para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.repositorio.RepositorioUsuarioSistema;
// Importa com.tiendasneto.ventas.servicio.ServicioAuditoria para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.servicio.ServicioAuditoria;
// Importa jakarta.validation.Valid para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.Valid;
// Importa org.springframework.http.HttpStatus para poder usar ese tipo o paquete en este archivo.
import org.springframework.http.HttpStatus;
// Importa org.springframework.http.ResponseEntity para poder usar ese tipo o paquete en este archivo.
import org.springframework.http.ResponseEntity;
// Importa org.springframework.security.access.prepost.PreAuthorize para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.access.prepost.PreAuthorize;
// Importa org.springframework.security.core.annotation.AuthenticationPrincipal para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.core.annotation.AuthenticationPrincipal;
// Importa org.springframework.security.core.userdetails.UserDetails para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.core.userdetails.UserDetails;
// Importa org.springframework.security.crypto.password.PasswordEncoder para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.crypto.password.PasswordEncoder;
// Importa org.springframework.transaction.annotation.Transactional para poder usar ese tipo o paquete en este archivo.
import org.springframework.transaction.annotation.Transactional;
// Importa org.springframework.web.bind.annotation.GetMapping para poder usar ese tipo o paquete en este archivo.
import org.springframework.web.bind.annotation.GetMapping;
// Importa org.springframework.web.bind.annotation.PostMapping para poder usar ese tipo o paquete en este archivo.
import org.springframework.web.bind.annotation.PostMapping;
// Importa org.springframework.web.bind.annotation.PutMapping para poder usar ese tipo o paquete en este archivo.
import org.springframework.web.bind.annotation.PutMapping;
// Importa org.springframework.web.bind.annotation.PathVariable para poder usar ese tipo o paquete en este archivo.
import org.springframework.web.bind.annotation.PathVariable;
// Importa org.springframework.web.bind.annotation.RequestBody para poder usar ese tipo o paquete en este archivo.
import org.springframework.web.bind.annotation.RequestBody;
// Importa org.springframework.web.bind.annotation.RequestMapping para poder usar ese tipo o paquete en este archivo.
import org.springframework.web.bind.annotation.RequestMapping;
// Importa org.springframework.web.bind.annotation.RestController para poder usar ese tipo o paquete en este archivo.
import org.springframework.web.bind.annotation.RestController;

// Importa java.util.List para poder usar ese tipo o paquete en este archivo.
import java.util.List;
// Importa java.util.Locale para poder usar ese tipo o paquete en este archivo.
import java.util.Locale;
// Importa java.util.Map para poder usar ese tipo o paquete en este archivo.
import java.util.Map;

/** Endpoints administrativos de cuentas, reservados para el encargado de tienda. */
// Aplica la anotación @RestController a la declaración que sigue.
@RestController
// Aplica la anotación @RequestMapping a la declaración que sigue.
@RequestMapping("/api/usuarios")
// Aplica la anotación @PreAuthorize a la declaración que sigue.
@PreAuthorize("hasRole('ENCARGADO')")
// Declara class ControladorAdministracionUsuarios y define su bloque de implementación.
public class ControladorAdministracionUsuarios {
    // Declara un atributo o constante disponible en el ámbito indicado.
    private final RepositorioUsuarioSistema usuarios;
    // Declara un atributo o constante disponible en el ámbito indicado.
    private final PasswordEncoder passwordEncoder;
    // Declara un atributo o constante disponible en el ámbito indicado.
    private final ServicioAuditoria auditoria;

    // Función: Constructor: crea un objeto ControladorAdministracionUsuarios con las dependencias que necesita.
    public ControladorAdministracionUsuarios(RepositorioUsuarioSistema usuarios,
            // Continúa la expresión o declaración iniciada en las líneas cercanas.
            PasswordEncoder passwordEncoder,
            // Continúa la expresión o declaración iniciada en las líneas cercanas.
            ServicioAuditoria auditoria) {
        // Asigna o consulta el atributo de esta instancia.
        this.usuarios = usuarios;
        // Asigna o consulta el atributo de esta instancia.
        this.passwordEncoder = passwordEncoder;
        // Asigna o consulta el atributo de esta instancia.
        this.auditoria = auditoria;
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    /** Devuelve la lista de perfiles sin incluir datos de contrasena. */
    // Aplica la anotación @GetMapping a la declaración que sigue.
    @GetMapping
    // Función: Consulta los datos de esta sección, los convierte a respuestas y los devuelve.
    public List<RespuestaUsuarioAdministrador> listar() {
        // Devuelve el resultado calculado por este método o bloque.
        return usuarios.findAll().stream()
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .sorted((a, b) -> a.getUsername().compareToIgnoreCase(b.getUsername()))
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .map(RespuestaUsuarioAdministrador::desde)
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .toList();
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    /** Crea una cuenta, codifica su contrasena y registra el evento en auditoria. */
    // Aplica la anotación @PostMapping a la declaración que sigue.
    @PostMapping
    // Aplica la anotación @Transactional a la declaración que sigue.
    @Transactional
    // Función: Valida los datos, guarda el registro y devuelve el resultado.
    public ResponseEntity<?> crear(@Valid @RequestBody SolicitudUsuario request,
            // Aplica la anotación @AuthenticationPrincipal a la declaración que sigue.
            @AuthenticationPrincipal UserDetails actor) {
        // Declara un valor local usado por la lógica de este bloque.
        String username = request.username().trim().toLowerCase(Locale.ROOT);
        // Controla el flujo de ejecución según esta condición o situación.
        if (usuarios.existsByUsernameIgnoreCase(username)) {
            // Devuelve el resultado calculado por este método o bloque.
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                    .body(Map.of("mensaje", "Ese nombre de usuario ya esta registrado."));
        // Cierra el bloque o la expresión que se definió anteriormente.
        }

        // Declara un valor local usado por la lógica de este bloque.
        UsuarioSistema nuevo = new UsuarioSistema(
                // Continúa la expresión o declaración iniciada en las líneas cercanas.
                username,
                // Continúa la expresión o declaración iniciada en las líneas cercanas.
                passwordEncoder.encode(request.password()),
                // Continúa la expresión o declaración iniciada en las líneas cercanas.
                request.nombre().trim(),
                // Ejecuta esta instrucción de la lógica actual.
                request.rol());

        // Declara un valor local usado por la lógica de este bloque.
        UsuarioSistema guardado = usuarios.saveAndFlush(nuevo);
        // Declara un valor local usado por la lógica de este bloque.
        String rolActor = actor.getAuthorities().stream()
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .findFirst()
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .map(authority -> authority.getAuthority().replace("ROLE_", "").toLowerCase(Locale.ROOT))
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .orElse("encargado");
        // Continúa la expresión o declaración iniciada en las líneas cercanas.
        auditoria.registrar(actor.getUsername(), rolActor, "CREAR", "Usuario", guardado.getId(),
                // Ejecuta esta instrucción de la lógica actual.
                "Cuenta " + guardado.getUsername() + " creada con rol " + guardado.getRol(), null);
        // Devuelve el resultado calculado por este método o bloque.
        return ResponseEntity.status(HttpStatus.CREATED).body(RespuestaUsuarioAdministrador.desde(guardado));
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    /** Modifica datos y permisos de una cuenta; la contrasena es opcional. */
    // Aplica la anotación @PutMapping a la declaración que sigue.
    @PutMapping("/{id}")
    // Aplica la anotación @Transactional a la declaración que sigue.
    @Transactional
    // Función: Busca el registro y guarda los cambios permitidos.
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody SolicitudActualizacionUsuario request,
            // Aplica la anotación @AuthenticationPrincipal a la declaración que sigue.
            @AuthenticationPrincipal UserDetails actor) {
        // Declara un valor local usado por la lógica de este bloque.
        var encontrada = usuarios.findById(id);
        // Controla el flujo de ejecución según esta condición o situación.
        if (encontrada.isEmpty()) {
            // Devuelve el resultado calculado por este método o bloque.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "No se encontro esa cuenta."));
        // Cierra el bloque o la expresión que se definió anteriormente.
        }

        // Declara un valor local usado por la lógica de este bloque.
        UsuarioSistema usuario = encontrada.get();
        // Declara un valor local usado por la lógica de este bloque.
        String username = request.username().trim().toLowerCase(Locale.ROOT);
        // Controla el flujo de ejecución según esta condición o situación.
        if (usuarios.existsByUsernameIgnoreCaseAndIdNot(username, id)) {
            // Devuelve el resultado calculado por este método o bloque.
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                    .body(Map.of("mensaje", "Ese nombre de usuario ya pertenece a otra cuenta."));
        // Cierra el bloque o la expresión que se definió anteriormente.
        }

        // Declara un valor local usado por la lógica de este bloque.
        boolean editaSuCuenta = usuario.getUsername().equalsIgnoreCase(actor.getUsername());
        // Controla el flujo de ejecución según esta condición o situación.
        if (editaSuCuenta && (!username.equalsIgnoreCase(usuario.getUsername())
                // Continúa la expresión o declaración iniciada en las líneas cercanas.
                || !"ENCARGADO".equals(request.rol()) || !request.activo())) {
            // Devuelve el resultado calculado por este método o bloque.
            return ResponseEntity.badRequest()
                    // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                    .body(Map.of("mensaje", "No puedes quitarte el acceso de encargado ni desactivar tu propia cuenta."));
        // Cierra el bloque o la expresión que se definió anteriormente.
        }

        // Declara un valor local usado por la lógica de este bloque.
        String anterior = usuario.getUsername() + " (" + usuario.getRol() + ")";
        // Ejecuta esta instrucción de la lógica actual.
        usuario.setUsername(username);
        // Ejecuta esta instrucción de la lógica actual.
        usuario.setNombre(request.nombre().trim());
        // Ejecuta esta instrucción de la lógica actual.
        usuario.setRol(request.rol());
        // Ejecuta esta instrucción de la lógica actual.
        usuario.setActivo(request.activo());
        // Controla el flujo de ejecución según esta condición o situación.
        if (request.nuevaContrasena() != null && !request.nuevaContrasena().isBlank()) {
            // Ejecuta esta instrucción de la lógica actual.
            usuario.setPasswordHash(passwordEncoder.encode(request.nuevaContrasena()));
        // Cierra el bloque o la expresión que se definió anteriormente.
        }

        // Declara un valor local usado por la lógica de este bloque.
        UsuarioSistema guardado = usuarios.saveAndFlush(usuario);
        // Declara un valor local usado por la lógica de este bloque.
        String rolActor = actor.getAuthorities().stream()
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .findFirst()
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .map(authority -> authority.getAuthority().replace("ROLE_", "").toLowerCase(Locale.ROOT))
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .orElse("encargado");
        // Continúa la expresión o declaración iniciada en las líneas cercanas.
        auditoria.registrar(actor.getUsername(), rolActor, "ACTUALIZAR", "Usuario", guardado.getId(),
                // Continúa la expresión o declaración iniciada en las líneas cercanas.
                anterior + " → " + guardado.getUsername() + " (" + guardado.getRol() + ", "
                        // Ejecuta esta instrucción de la lógica actual.
                        + (guardado.isActivo() ? "activo" : "inactivo") + ")", null);
        // Devuelve el resultado calculado por este método o bloque.
        return ResponseEntity.ok(RespuestaUsuarioAdministrador.desde(guardado));
    // Cierra el bloque o la expresión que se definió anteriormente.
    }
// Cierra el bloque o la expresión que se definió anteriormente.
}

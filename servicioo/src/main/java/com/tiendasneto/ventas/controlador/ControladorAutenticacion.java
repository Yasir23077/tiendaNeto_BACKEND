// Archivo: src/main/java/com/tiendasneto/ventas/controlador/ControladorAutenticacion.java. Funcion: Atiende el login, la sesion, el perfil y el cierre de sesion.
// Declara el paquete com.tiendasneto.ventas.controlador al que pertenece esta clase.
package com.tiendasneto.ventas.controlador;

// Importa com.tiendasneto.ventas.transferencia.solicitud.SolicitudInicioSesion para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.transferencia.solicitud.SolicitudInicioSesion;
// Importa com.tiendasneto.ventas.transferencia.respuesta.RespuestaUsuario para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.transferencia.respuesta.RespuestaUsuario;
// Importa com.tiendasneto.ventas.repositorio.RepositorioUsuarioSistema para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.repositorio.RepositorioUsuarioSistema;
// Importa jakarta.servlet.http.HttpServletRequest para poder usar ese tipo o paquete en este archivo.
import jakarta.servlet.http.HttpServletRequest;
// Importa jakarta.servlet.http.HttpServletResponse para poder usar ese tipo o paquete en este archivo.
import jakarta.servlet.http.HttpServletResponse;
// Importa jakarta.validation.Valid para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.Valid;
// Importa org.springframework.http.HttpStatus para poder usar ese tipo o paquete en este archivo.
import org.springframework.http.HttpStatus;
// Importa org.springframework.http.ResponseEntity para poder usar ese tipo o paquete en este archivo.
import org.springframework.http.ResponseEntity;
// Importa org.springframework.security.authentication.AuthenticationManager para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.authentication.AuthenticationManager;
// Importa org.springframework.security.authentication.AuthenticationServiceException para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.authentication.AuthenticationServiceException;
// Importa org.springframework.security.authentication.UsernamePasswordAuthenticationToken para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// Importa org.springframework.security.core.Authentication para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.core.Authentication;
// Importa org.springframework.security.core.AuthenticationException para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.core.AuthenticationException;
// Importa org.springframework.security.core.context.SecurityContext para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.core.context.SecurityContext;
// Importa org.springframework.security.core.context.SecurityContextHolder para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.core.context.SecurityContextHolder;
// Importa org.springframework.security.web.context.SecurityContextRepository para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.web.context.SecurityContextRepository;
// Importa org.springframework.security.web.csrf.CsrfToken para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.web.csrf.CsrfToken;
// Importa org.springframework.web.bind.annotation.* para poder usar ese tipo o paquete en este archivo.
import org.springframework.web.bind.annotation.*;

// Importa java.util.Map para poder usar ese tipo o paquete en este archivo.
import java.util.Map;

// Aplica la anotación @RestController a la declaración que sigue.
@RestController
// Aplica la anotación @RequestMapping a la declaración que sigue.
@RequestMapping("/api/auth")
// Declara class ControladorAutenticacion y define su bloque de implementación.
public class ControladorAutenticacion {
    // Componente que valida el usuario y la contrasena recibidos.
    private final AuthenticationManager authenticationManager;
    // Guarda y recupera la identidad autenticada en la sesion HTTP.
    private final SecurityContextRepository securityContextRepository;
    // Busca los datos de cuenta usados en la respuesta segura del perfil.
    private final RepositorioUsuarioSistema usuarios;

    // Recibe los componentes de autenticacion, sesion y consulta de cuentas.
    // Función: Constructor: crea un objeto ControladorAutenticacion con las dependencias que necesita.
    public ControladorAutenticacion(AuthenticationManager authenticationManager,
            // Continúa la expresión o declaración iniciada en las líneas cercanas.
            SecurityContextRepository securityContextRepository,
            // Continúa la expresión o declaración iniciada en las líneas cercanas.
            RepositorioUsuarioSistema usuarios) {
        // Asigna o consulta el atributo de esta instancia.
        this.authenticationManager = authenticationManager;
        // Asigna o consulta el atributo de esta instancia.
        this.securityContextRepository = securityContextRepository;
        // Asigna o consulta el atributo de esta instancia.
        this.usuarios = usuarios;
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // GET /api/auth/csrf entrega el token requerido al modificar datos.
    @GetMapping("/csrf")
    // Función: Entrega al navegador el token CSRF requerido para cambios.
    public Map<String, String> csrf(CsrfToken csrfToken) {
        // Devuelve el nombre del encabezado y el token que espera Spring Security.
        return Map.of("headerName", csrfToken.getHeaderName(), "token", csrfToken.getToken());
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // POST /api/auth/login valida credenciales y crea una sesion en el servidor.
    @PostMapping("/login")
    // Función: Valida credenciales, crea la sesión y devuelve un perfil seguro.
    public ResponseEntity<?> iniciarSesion(@Valid @RequestBody SolicitudInicioSesion request,
            // Continúa la expresión o declaración iniciada en las líneas cercanas.
            HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        // Controla el flujo de ejecución según esta condición o situación.
        try {
            // Delega la comprobacion de credenciales al AuthenticationManager.
            Authentication authentication = authenticationManager.authenticate(
                    // Continúa la expresión o declaración iniciada en las líneas cercanas.
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            // Ejecuta esta instrucción de la lógica actual.
                            request.username().trim(), request.password()));
            // Renueva el identificador de sesion despues del login.
            if (httpRequest.getSession(false) != null) httpRequest.changeSessionId();
            // Crea un contexto de seguridad nuevo para el usuario autenticado.
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            // Ejecuta esta instrucción de la lógica actual.
            context.setAuthentication(authentication);
            // Establece la autenticacion actual y la guarda para siguientes solicitudes.
            SecurityContextHolder.setContext(context);
            // Ejecuta esta instrucción de la lógica actual.
            securityContextRepository.saveContext(context, httpRequest, httpResponse);
            // Consulta los datos del perfil sin incluir el hash de contrasena.
            var usuario = usuarios.findByUsernameIgnoreCase(authentication.getName())
                    // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                    .orElseThrow(() -> new AuthenticationServiceException("No se encontró el perfil."));
            // Devuelve el perfil publico y el rol que necesita mostrar la interfaz.
            return ResponseEntity.ok(RespuestaUsuario.desde(usuario));
        // Continúa la expresión o declaración iniciada en las líneas cercanas.
        } catch (AuthenticationException exception) {
            // Limpia la autenticacion parcial si las credenciales fueron rechazadas.
            SecurityContextHolder.clearContext();
            // Devuelve 401 con mensaje generico para no revelar que dato fallo.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                    .body(Map.of("mensaje", "El usuario o la contraseña no son correctos."));
        // Cierra el bloque o la expresión que se definió anteriormente.
        }
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // GET /api/auth/me devuelve el perfil asociado a la sesion actual.
    @GetMapping("/me")
    // Función: Devuelve el perfil público asociado a la sesión actual.
    public RespuestaUsuario sesionActual(Authentication authentication) {
        // Busca la cuenta con el usuario autenticado por Spring Security.
        var usuario = usuarios.findByUsernameIgnoreCase(authentication.getName())
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .orElseThrow(() -> new AuthenticationServiceException("No se encontró el perfil."));
        // Convierte la entidad a una respuesta que no expone passwordHash.
        return RespuestaUsuario.desde(usuario);
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // POST /api/auth/logout cierra la sesion autenticada actual.
    @PostMapping("/logout")
    // Función: Limpia la autenticación e invalida la sesión HTTP.
    public ResponseEntity<Void> cerrarSesion(HttpServletRequest request) {
        // Elimina la autenticacion del contexto de la solicitud actual.
        SecurityContextHolder.clearContext();
        // Recupera la sesion actual sin crear una nueva.
        var sesion = request.getSession(false);
        // Invalida la sesion para que no autentique solicitudes posteriores.
        if (sesion != null) sesion.invalidate();
        // Devuelve HTTP 204 para indicar cierre correcto sin cuerpo de respuesta.
        return ResponseEntity.noContent().build();
    // Cierra el bloque o la expresión que se definió anteriormente.
    }
// Cierra el bloque o la expresión que se definió anteriormente.
}

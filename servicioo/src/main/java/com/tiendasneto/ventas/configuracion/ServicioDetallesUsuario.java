// Archivo: src/main/java/com/tiendasneto/ventas/configuracion/ServicioDetallesUsuario.java. Funcion: Busca cuentas y prepara sus roles para Spring Security.
// Declara el paquete com.tiendasneto.ventas.configuracion al que pertenece esta clase.
package com.tiendasneto.ventas.configuracion;

// Importa com.tiendasneto.ventas.repositorio.RepositorioUsuarioSistema para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.repositorio.RepositorioUsuarioSistema;
// Importa org.springframework.security.core.userdetails.User para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.core.userdetails.User;
// Importa org.springframework.security.core.userdetails.UserDetails para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.core.userdetails.UserDetails;
// Importa org.springframework.security.core.userdetails.UserDetailsService para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.core.userdetails.UserDetailsService;
// Importa org.springframework.security.core.userdetails.UsernameNotFoundException para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.core.userdetails.UsernameNotFoundException;
// Importa org.springframework.stereotype.Service para poder usar ese tipo o paquete en este archivo.
import org.springframework.stereotype.Service;
// Importa java.util.Locale para poder usar ese tipo o paquete en este archivo.
import java.util.Locale;
// Importa java.util.Set para poder usar ese tipo o paquete en este archivo.
import java.util.Set;

// Aplica la anotación @Service a la declaración que sigue.
@Service
// Declara class ServicioDetallesUsuario y define su bloque de implementación.
public class ServicioDetallesUsuario implements UserDetailsService {
    // Roles aceptados por el sistema; un usuario con otro valor no puede autenticarse.
    private static final Set<String> ROLES_VALIDOS = Set.of("CAJERO", "LIDER", "ENCARGADO");
    // Repositorio que busca las cuentas registradas durante el inicio de sesion.
    private final RepositorioUsuarioSistema usuarios;

    // Spring inyecta el repositorio usado para buscar usuarios.
    // Función: Constructor: crea un objeto ServicioDetallesUsuario con las dependencias que necesita.
    public ServicioDetallesUsuario(RepositorioUsuarioSistema usuarios) {
        // Asigna o consulta el atributo de esta instancia.
        this.usuarios = usuarios;
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Spring Security invoca este metodo con el usuario enviado desde el formulario.
    @Override
    // Función: Busca la cuenta, comprueba su estado y rol y la prepara para Spring Security.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca sin distinguir mayusculas y rechaza las cuentas desactivadas.
        var usuario = usuarios.findByUsernameIgnoreCase(username)
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .filter(u -> u.isActivo())
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .orElseThrow(() -> new UsernameNotFoundException("Usuario o contraseña incorrectos."));
        // Normaliza el rol de MySQL para que coincida con hasRole/hasAnyRole.
        String rol = usuario.getRol() == null ? "" : usuario.getRol().trim().toUpperCase(Locale.ROOT);
        // Controla el flujo de ejecución según esta condición o situación.
        if (!ROLES_VALIDOS.contains(rol)) {
            // Interrumpe el flujo normal y propaga una excepción.
            throw new UsernameNotFoundException("El usuario tiene un rol no valido. Contacta al encargado.");
        // Cierra el bloque o la expresión que se definió anteriormente.
        }
        // Convierte la entidad del proyecto al formato UserDetails de Spring Security.
        return User.withUsername(usuario.getUsername())
                // Entrega el hash guardado; la contrasena original no se persiste.
                .password(usuario.getPasswordHash())
                // Agrega ROLE_, prefijo que utilizan las reglas hasRole.
                .authorities("ROLE_" + rol)
                // Construye la informacion que AuthenticationManager utilizara.
                .build();
    // Cierra el bloque o la expresión que se definió anteriormente.
    }
// Cierra el bloque o la expresión que se definió anteriormente.
}

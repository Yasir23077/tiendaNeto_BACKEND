// Archivo: src/main/java/com/tiendasneto/ventas/configuracion/UsuariosIniciales.java. Funcion: Crea cuentas iniciales de demostracion si todavia no existen.
// Declara el paquete com.tiendasneto.ventas.configuracion al que pertenece esta clase.
package com.tiendasneto.ventas.configuracion;

// Importa com.tiendasneto.ventas.modelo.UsuarioSistema para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.UsuarioSistema;
// Importa com.tiendasneto.ventas.repositorio.RepositorioUsuarioSistema para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.repositorio.RepositorioUsuarioSistema;
// Importa org.springframework.beans.factory.annotation.Value para poder usar ese tipo o paquete en este archivo.
import org.springframework.beans.factory.annotation.Value;
// Importa org.springframework.boot.CommandLineRunner para poder usar ese tipo o paquete en este archivo.
import org.springframework.boot.CommandLineRunner;
// Importa org.springframework.security.crypto.password.PasswordEncoder para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.crypto.password.PasswordEncoder;
// Importa org.springframework.stereotype.Component para poder usar ese tipo o paquete en este archivo.
import org.springframework.stereotype.Component;

// Aplica la anotación @Component a la declaración que sigue.
@Component
// Declara class UsuariosIniciales y define su bloque de implementación.
public class UsuariosIniciales implements CommandLineRunner {
    // Repositorio para consultar y guardar cuentas del sistema.
    private final RepositorioUsuarioSistema usuarios;
    // BCrypt codifica las contrasenas antes de guardarlas en la base de datos.
    private final PasswordEncoder passwordEncoder;

    // Opcion para habilitar o deshabilitar las cuentas iniciales de demostracion.
    @Value("${app.security.demo-users.enabled:true}")
    // Declara un atributo o constante disponible en el ámbito indicado.
    private boolean habilitarUsuariosDemo;
    // Contrasena inicial del cajero; se puede reemplazar con una variable de entorno.
    @Value("${app.security.demo-users.cajero-password}")
    // Declara un atributo o constante disponible en el ámbito indicado.
    private String passwordCajero;
    // Contrasena inicial del lider; se puede reemplazar con una variable de entorno.
    @Value("${app.security.demo-users.lider-password}")
    // Declara un atributo o constante disponible en el ámbito indicado.
    private String passwordLider;
    // Contrasena inicial del encargado; se puede reemplazar con una variable de entorno.
    @Value("${app.security.demo-users.encargado-password}")
    // Declara un atributo o constante disponible en el ámbito indicado.
    private String passwordEncargado;

    // Recibe las dependencias mediante la inyeccion de Spring.
    // Función: Constructor: crea un objeto UsuariosIniciales con las dependencias que necesita.
    public UsuariosIniciales(RepositorioUsuarioSistema usuarios, PasswordEncoder passwordEncoder) {
        // Asigna o consulta el atributo de esta instancia.
        this.usuarios = usuarios;
        // Asigna o consulta el atributo de esta instancia.
        this.passwordEncoder = passwordEncoder;
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Spring ejecuta este metodo una vez al iniciar el contexto de la aplicacion.
    @Override
    // Función: Spring ejecuta este método al iniciar para cargar datos iniciales.
    public void run(String... args) {
        // Termina sin crear cuentas cuando la opcion esta desactivada.
        if (!habilitarUsuariosDemo) return;
        // Crea las cuentas de desarrollo si no existen, sin reemplazar usuarios previos.
        crearSiNoExiste("cajero", passwordCajero, "Alex Rivera", "CAJERO");
        // Ejecuta esta instrucción de la lógica actual.
        crearSiNoExiste("lider", passwordLider, "Mariana Torres", "LIDER");
        // Ejecuta esta instrucción de la lógica actual.
        crearSiNoExiste("encargado", passwordEncargado, "Daniel Morales", "ENCARGADO");
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Crea una cuenta solo si el nombre de usuario aun no esta registrado.
    // Función: Crea una cuenta de demostración solo si el nombre aún no está registrado.
    private void crearSiNoExiste(String username, String password, String nombre, String rol) {
        // Conserva la cuenta existente y su contrasena actual.
        if (usuarios.existsByUsernameIgnoreCase(username)) return;
        // Codifica la contrasena, construye la entidad y la guarda en la base de datos.
        usuarios.save(new UsuarioSistema(username, passwordEncoder.encode(password), nombre, rol));
    // Cierra el bloque o la expresión que se definió anteriormente.
    }
// Cierra el bloque o la expresión que se definió anteriormente.
}

// Archivo: src/main/java/com/tiendasneto/ventas/repositorio/RepositorioUsuarioSistema.java. Funcion: Busca y guarda las cuentas del sistema.
// Declara el paquete com.tiendasneto.ventas.repositorio al que pertenece esta clase.
package com.tiendasneto.ventas.repositorio;

// Importa com.tiendasneto.ventas.modelo.UsuarioSistema para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.UsuarioSistema;
// Importa org.springframework.data.jpa.repository.JpaRepository para poder usar ese tipo o paquete en este archivo.
import org.springframework.data.jpa.repository.JpaRepository;

// Importa java.util.Optional para poder usar ese tipo o paquete en este archivo.
import java.util.Optional;

// Repositorio JPA para consultar cuentas guardadas en usuarios_sistema.
public interface RepositorioUsuarioSistema extends JpaRepository<UsuarioSistema, Long> {
    // Busca una cuenta sin distinguir mayusculas en el nombre de usuario.
    Optional<UsuarioSistema> findByUsernameIgnoreCase(String username);
    // Comprueba si ya existe el usuario antes de crear una cuenta inicial.
    boolean existsByUsernameIgnoreCase(String username);
    // Comprueba duplicados al editar, excluyendo la cuenta actual.
    boolean existsByUsernameIgnoreCaseAndIdNot(String username, Long id);
// Cierra el bloque o la expresión que se definió anteriormente.
}

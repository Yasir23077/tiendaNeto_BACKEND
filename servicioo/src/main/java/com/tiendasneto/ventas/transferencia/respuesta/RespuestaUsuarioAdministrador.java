// Archivo: src/main/java/com/tiendasneto/ventas/transferencia/respuesta/RespuestaUsuarioAdministrador.java. Funcion: Define datos de administracion de usuario sin contrasena.
// Declara el paquete com.tiendasneto.ventas.transferencia.respuesta al que pertenece esta clase.
package com.tiendasneto.ventas.transferencia.respuesta;

// Importa com.tiendasneto.ventas.modelo.UsuarioSistema para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.UsuarioSistema;

/** Perfil para la vista administrativa; excluye siempre el hash de contrasena. */
// DTO: Define los datos que viajan por la API; Java genera automáticamente su constructor y métodos de acceso.
public record RespuestaUsuarioAdministrador(Long id, String username, String nombre, String rol, boolean activo) {
    // Función: Convierte una entidad de base de datos a una respuesta segura de la API.
    public static RespuestaUsuarioAdministrador desde(UsuarioSistema usuario) {
        // Devuelve el resultado calculado por este método o bloque.
        return new RespuestaUsuarioAdministrador(
                // Continúa la expresión o declaración iniciada en las líneas cercanas.
                usuario.getId(),
                // Continúa la expresión o declaración iniciada en las líneas cercanas.
                usuario.getUsername(),
                // Continúa la expresión o declaración iniciada en las líneas cercanas.
                usuario.getNombre(),
                // Continúa la expresión o declaración iniciada en las líneas cercanas.
                usuario.getRol(),
                // Ejecuta esta instrucción de la lógica actual.
                usuario.isActivo());
    // Cierra el bloque o la expresión que se definió anteriormente.
    }
// Cierra el bloque o la expresión que se definió anteriormente.
}

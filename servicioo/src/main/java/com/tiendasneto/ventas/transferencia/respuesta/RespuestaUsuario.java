// Archivo: src/main/java/com/tiendasneto/ventas/transferencia/respuesta/RespuestaUsuario.java. Funcion: Define el perfil seguro que recibe el usuario.
// Declara el paquete com.tiendasneto.ventas.transferencia.respuesta al que pertenece esta clase.
package com.tiendasneto.ventas.transferencia.respuesta;

// Importa com.tiendasneto.ventas.modelo.UsuarioSistema para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.UsuarioSistema;

// Perfil publico de sesion; nunca incluye el hash de contrasena de la entidad.
// DTO: Define los datos que viajan por la API; Java genera automáticamente su constructor y métodos de acceso.
public record RespuestaUsuario(String username, String name, String role, String roleLabel, String initials) {
    // Convierte la entidad de usuario en los datos que necesita mostrar el frontend.
    // Función: Convierte una entidad de base de datos a una respuesta segura de la API.
    public static RespuestaUsuario desde(UsuarioSistema usuario) {
        // Traduce el rol interno a una etiqueta legible.
        String roleLabel = switch (usuario.getRol()) {
            // Controla el flujo de ejecución según esta condición o situación.
            case "ENCARGADO" -> "Encargado de tienda";
            // Controla el flujo de ejecución según esta condición o situación.
            case "LIDER" -> "Líder de tienda";
            // Ejecuta esta instrucción de la lógica actual.
            default -> "Cajero";
        // Cierra el bloque o la expresión que se definió anteriormente.
        };
        // Obtiene iniciales de las primeras dos palabras del nombre.
        String initials = java.util.Arrays.stream(usuario.getNombre().trim().split("\\s+"))
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .limit(2).map(palabra -> palabra.substring(0, 1).toUpperCase())
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .reduce("", String::concat);
        // Devuelve los datos publicos sin exponer passwordHash.
        return new RespuestaUsuario(usuario.getUsername(), usuario.getNombre(),
                // Ejecuta esta instrucción de la lógica actual.
                usuario.getRol().toLowerCase(), roleLabel, initials);
    // Cierra el bloque o la expresión que se definió anteriormente.
    }
// Cierra el bloque o la expresión que se definió anteriormente.
}

// Archivo: src/main/java/com/tiendasneto/ventas/transferencia/respuesta/RespuestaAuditoria.java. Funcion: Define los datos de auditoria que devuelve la API.
// Declara el paquete com.tiendasneto.ventas.transferencia.respuesta al que pertenece esta clase.
package com.tiendasneto.ventas.transferencia.respuesta;

// Importa com.tiendasneto.ventas.modelo.RegistroAuditoria para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.RegistroAuditoria;
// Importa java.time.LocalDateTime para poder usar ese tipo o paquete en este archivo.
import java.time.LocalDateTime;

/** Campos de auditoría visibles en el historial administrativo. */
// DTO: Define los datos que viajan por la API; Java genera automáticamente su constructor y métodos de acceso.
public record RespuestaAuditoria(Long id, LocalDateTime fecha, String usuario, String rol,
                                // Declara un valor local usado por la lógica de este bloque.
                                String accion, String entidad, Long entidadId, String detalle, String sucursal) {
    // Función: Convierte una entidad de base de datos a una respuesta segura de la API.
    public static RespuestaAuditoria desde(RegistroAuditoria registro) {
        // Devuelve el resultado calculado por este método o bloque.
        return new RespuestaAuditoria(registro.getId(), registro.getFecha(), registro.getUsuario(), registro.getRol(),
                // Ejecuta esta instrucción de la lógica actual.
                registro.getAccion(), registro.getEntidad(), registro.getEntidadId(), registro.getDetalle(), registro.getSucursal());
    // Cierra el bloque o la expresión que se definió anteriormente.
    }
// Cierra el bloque o la expresión que se definió anteriormente.
}

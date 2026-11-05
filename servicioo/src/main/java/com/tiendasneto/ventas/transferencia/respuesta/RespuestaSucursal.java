// Archivo: src/main/java/com/tiendasneto/ventas/transferencia/respuesta/RespuestaSucursal.java. Funcion: Define los datos de sucursal que devuelve la API.
// Declara el paquete com.tiendasneto.ventas.transferencia.respuesta al que pertenece esta clase.
package com.tiendasneto.ventas.transferencia.respuesta;

// Importa com.tiendasneto.ventas.modelo.Sucursal para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.Sucursal;

/** Campos de sucursal que la API entrega al frontend. */
// DTO: Define los datos que viajan por la API; Java genera automáticamente su constructor y métodos de acceso.
public record RespuestaSucursal(Long id, String codigo, String nombre, boolean activa) {
    // Función: Convierte una entidad de base de datos a una respuesta segura de la API.
    public static RespuestaSucursal desde(Sucursal sucursal) {
        // Devuelve el resultado calculado por este método o bloque.
        return new RespuestaSucursal(sucursal.getId(), sucursal.getCodigo(), sucursal.getNombre(), sucursal.isActiva());
    // Cierra el bloque o la expresión que se definió anteriormente.
    }
// Cierra el bloque o la expresión que se definió anteriormente.
}

// Archivo: src/main/java/com/tiendasneto/ventas/transferencia/respuesta/RespuestaVenta.java. Funcion: Define los datos de venta que devuelve la API.
// Declara el paquete com.tiendasneto.ventas.transferencia.respuesta al que pertenece esta clase.
package com.tiendasneto.ventas.transferencia.respuesta;

// Importa com.tiendasneto.ventas.modelo.Venta para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.Venta;
// Importa java.math.BigDecimal para poder usar ese tipo o paquete en este archivo.
import java.math.BigDecimal;
// Importa java.time.LocalDateTime para poder usar ese tipo o paquete en este archivo.
import java.time.LocalDateTime;

/** Representación pública de una venta; excluye detalles internos de persistencia. */
// DTO: Define los datos que viajan por la API; Java genera automáticamente su constructor y métodos de acceso.
public record RespuestaVenta(Long id, String folio, String producto, Integer cantidad, BigDecimal precioUnitario,
                            // Continúa la expresión o declaración iniciada en las líneas cercanas.
                            BigDecimal total, LocalDateTime fecha, String sucursal) {
    // Función: Convierte una entidad de base de datos a una respuesta segura de la API.
    public static RespuestaVenta desde(Venta venta) {
        // Devuelve el resultado calculado por este método o bloque.
        return new RespuestaVenta(venta.getId(), venta.getFolio(), venta.getProducto(), venta.getCantidad(),
                // Ejecuta esta instrucción de la lógica actual.
                venta.getPrecioUnitario(), venta.getTotal(), venta.getFecha(), venta.getSucursal());
    // Cierra el bloque o la expresión que se definió anteriormente.
    }
// Cierra el bloque o la expresión que se definió anteriormente.
}

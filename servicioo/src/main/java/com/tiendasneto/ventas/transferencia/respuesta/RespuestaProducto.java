// Archivo: src/main/java/com/tiendasneto/ventas/transferencia/respuesta/RespuestaProducto.java. Funcion: Define los datos de producto que devuelve la API.
// Declara el paquete com.tiendasneto.ventas.transferencia.respuesta al que pertenece esta clase.
package com.tiendasneto.ventas.transferencia.respuesta;

// Importa com.tiendasneto.ventas.modelo.Producto para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.Producto;
// Importa java.math.BigDecimal para poder usar ese tipo o paquete en este archivo.
import java.math.BigDecimal;

/** Campos de producto que la API entrega al frontend. */
// DTO: Define los datos que viajan por la API; Java genera automáticamente su constructor y métodos de acceso.
public record RespuestaProducto(Long id, String codigo, String nombre, String categoria,
                               // Continúa la expresión o declaración iniciada en las líneas cercanas.
                               BigDecimal precio, boolean activo) {
    // Función: Convierte una entidad de base de datos a una respuesta segura de la API.
    public static RespuestaProducto desde(Producto producto) {
        // Devuelve el resultado calculado por este método o bloque.
        return new RespuestaProducto(producto.getId(), producto.getCodigo(), producto.getNombre(),
                // Ejecuta esta instrucción de la lógica actual.
                producto.getCategoria(), producto.getPrecio(), producto.isActivo());
    // Cierra el bloque o la expresión que se definió anteriormente.
    }
// Cierra el bloque o la expresión que se definió anteriormente.
}

// Archivo: transferencia/solicitud/SolicitudVentaMultiple.java. Funcion: Recibe los articulos de un mismo cobro.
// Declara el paquete com.tiendasneto.ventas.transferencia.solicitud al que pertenece esta clase.
package com.tiendasneto.ventas.transferencia.solicitud;

// Importa jakarta.validation.Valid para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.Valid;
// Importa jakarta.validation.constraints.NotBlank para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.NotBlank;
// Importa jakarta.validation.constraints.NotEmpty para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.NotEmpty;
// Importa jakarta.validation.constraints.NotNull para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.NotNull;
// Importa jakarta.validation.constraints.Positive para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.Positive;
// Importa jakarta.validation.constraints.Size para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.Size;
// Importa java.util.List para poder usar ese tipo o paquete en este archivo.
import java.util.List;

/** Datos de una compra con varios productos. */
// DTO: Define los datos que viajan por la API; Java genera automáticamente su constructor y métodos de acceso.
public record SolicitudVentaMultiple(
        // Aplica la anotación @NotBlank a la declaración que sigue.
        @NotBlank(message = "Selecciona una sucursal.")
        // Aplica la anotación @Size a la declaración que sigue.
        @Size(max = 140, message = "El nombre de sucursal no puede superar 140 caracteres.")
        // Declara un valor local usado por la lógica de este bloque.
        String sucursal,

        // Aplica la anotación @NotEmpty a la declaración que sigue.
        @NotEmpty(message = "Agrega al menos un producto a la venta.")
        // Aplica la anotación @Size a la declaración que sigue.
        @Size(max = 100, message = "Una venta admite hasta 100 productos diferentes.")
        // Declara un valor local usado por la lógica de este bloque.
        List<@Valid Detalle> productos
// Continúa la expresión o declaración iniciada en las líneas cercanas.
) {
    /** Identificador y cantidad de un producto incluido en el cobro. */
    // DTO anidado: representa una línea del carrito con el identificador del producto y la cantidad.
    public record Detalle(
            // Aplica la anotación @NotNull a la declaración que sigue.
            @NotNull(message = "Selecciona un producto.")
            // Aplica la anotación @Positive a la declaración que sigue.
            @Positive(message = "El identificador del producto no es valido.")
            // Declara un valor local usado por la lógica de este bloque.
            Long productoId,

            // Aplica la anotación @NotNull a la declaración que sigue.
            @NotNull(message = "La cantidad es obligatoria.")
            // Aplica la anotación @Positive a la declaración que sigue.
            @Positive(message = "La cantidad debe ser mayor que cero.")
            // Declara un valor local usado por la lógica de este bloque.
            Integer cantidad
    // Continúa la expresión o declaración iniciada en las líneas cercanas.
    ) {}
// Cierra el bloque o la expresión que se definió anteriormente.
}

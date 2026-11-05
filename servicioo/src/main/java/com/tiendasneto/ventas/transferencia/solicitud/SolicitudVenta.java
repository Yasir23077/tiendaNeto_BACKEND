// Archivo: src/main/java/com/tiendasneto/ventas/transferencia/solicitud/SolicitudVenta.java. Funcion: Define y valida los datos recibidos para una venta.
// Declara el paquete com.tiendasneto.ventas.transferencia.solicitud al que pertenece esta clase.
package com.tiendasneto.ventas.transferencia.solicitud;

// Importa jakarta.validation.constraints.DecimalMin para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.DecimalMin;
// Importa jakarta.validation.constraints.Digits para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.Digits;
// Importa jakarta.validation.constraints.NotBlank para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.NotBlank;
// Importa jakarta.validation.constraints.NotNull para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.NotNull;
// Importa jakarta.validation.constraints.Positive para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.Positive;
// Importa jakarta.validation.constraints.Size para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.Size;
// Importa java.math.BigDecimal para poder usar ese tipo o paquete en este archivo.
import java.math.BigDecimal;

/** Datos que el cliente puede enviar para registrar o actualizar una venta. */
// DTO: Define los datos que viajan por la API; Java genera automáticamente su constructor y métodos de acceso.
public record SolicitudVenta(
        // Aplica la anotación @NotBlank a la declaración que sigue.
        @NotBlank(message = "El producto es obligatorio.")
        // Aplica la anotación @Size a la declaración que sigue.
        @Size(max = 255, message = "El nombre del producto no puede superar 255 caracteres.")
        // Declara un valor local usado por la lógica de este bloque.
        String producto,

        // Aplica la anotación @NotNull a la declaración que sigue.
        @NotNull(message = "La cantidad es obligatoria.")
        // Aplica la anotación @Positive a la declaración que sigue.
        @Positive(message = "La cantidad debe ser mayor que cero.")
        // Declara un valor local usado por la lógica de este bloque.
        Integer cantidad,

        // Aplica la anotación @NotNull a la declaración que sigue.
        @NotNull(message = "El precio unitario es obligatorio.")
        // Aplica la anotación @DecimalMin a la declaración que sigue.
        @DecimalMin(value = "0.00", message = "El precio no puede ser negativo.")
        // Aplica la anotación @Digits a la declaración que sigue.
        @Digits(integer = 8, fraction = 2, message = "El precio admite hasta 8 enteros y 2 decimales.")
        // Continúa la expresión o declaración iniciada en las líneas cercanas.
        BigDecimal precioUnitario,

        // Aplica la anotación @NotBlank a la declaración que sigue.
        @NotBlank(message = "La sucursal es obligatoria.")
        // Aplica la anotación @Size a la declaración que sigue.
        @Size(max = 140, message = "El nombre de sucursal no puede superar 140 caracteres.")
        // Declara un valor local usado por la lógica de este bloque.
        String sucursal
// Continúa la expresión o declaración iniciada en las líneas cercanas.
) {}

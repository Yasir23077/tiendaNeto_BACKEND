// Archivo: src/main/java/com/tiendasneto/ventas/transferencia/solicitud/SolicitudProducto.java. Funcion: Define y valida los datos recibidos para un producto.
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
// Importa jakarta.validation.constraints.Size para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.Size;
// Importa java.math.BigDecimal para poder usar ese tipo o paquete en este archivo.
import java.math.BigDecimal;

/** Campos permitidos para crear o modificar un producto del catálogo. */
// DTO: Define los datos que viajan por la API; Java genera automáticamente su constructor y métodos de acceso.
public record SolicitudProducto(
        // Aplica la anotación @NotBlank a la declaración que sigue.
        @NotBlank(message = "El código es obligatorio.")
        // Aplica la anotación @Size a la declaración que sigue.
        @Size(max = 40, message = "El código no puede superar 40 caracteres.")
        // Declara un valor local usado por la lógica de este bloque.
        String codigo,

        // Aplica la anotación @NotBlank a la declaración que sigue.
        @NotBlank(message = "El nombre es obligatorio.")
        // Aplica la anotación @Size a la declaración que sigue.
        @Size(max = 160, message = "El nombre no puede superar 160 caracteres.")
        // Declara un valor local usado por la lógica de este bloque.
        String nombre,

        // Aplica la anotación @Size a la declaración que sigue.
        @Size(max = 80, message = "La categoría no puede superar 80 caracteres.")
        // Declara un valor local usado por la lógica de este bloque.
        String categoria,

        // Aplica la anotación @NotNull a la declaración que sigue.
        @NotNull(message = "El precio es obligatorio.")
        // Aplica la anotación @DecimalMin a la declaración que sigue.
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero.")
        // Aplica la anotación @Digits a la declaración que sigue.
        @Digits(integer = 8, fraction = 2, message = "El precio admite hasta 8 enteros y 2 decimales.")
        // Continúa la expresión o declaración iniciada en las líneas cercanas.
        BigDecimal precio
// Continúa la expresión o declaración iniciada en las líneas cercanas.
) {}

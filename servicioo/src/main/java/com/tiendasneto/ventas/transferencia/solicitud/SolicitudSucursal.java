// Archivo: src/main/java/com/tiendasneto/ventas/transferencia/solicitud/SolicitudSucursal.java. Funcion: Define y valida los datos recibidos para una sucursal.
// Declara el paquete com.tiendasneto.ventas.transferencia.solicitud al que pertenece esta clase.
package com.tiendasneto.ventas.transferencia.solicitud;

// Importa jakarta.validation.constraints.NotBlank para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.NotBlank;
// Importa jakarta.validation.constraints.Size para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.Size;

/** Datos permitidos para registrar una sucursal. */
// DTO: Define los datos que viajan por la API; Java genera automáticamente su constructor y métodos de acceso.
public record SolicitudSucursal(
        // Aplica la anotación @NotBlank a la declaración que sigue.
        @NotBlank(message = "El código es obligatorio.")
        // Aplica la anotación @Size a la declaración que sigue.
        @Size(max = 30, message = "El código no puede superar 30 caracteres.")
        // Declara un valor local usado por la lógica de este bloque.
        String codigo,

        // Aplica la anotación @NotBlank a la declaración que sigue.
        @NotBlank(message = "El nombre es obligatorio.")
        // Aplica la anotación @Size a la declaración que sigue.
        @Size(max = 140, message = "El nombre no puede superar 140 caracteres.")
        // Declara un valor local usado por la lógica de este bloque.
        String nombre
// Continúa la expresión o declaración iniciada en las líneas cercanas.
) {}

// Archivo: src/main/java/com/tiendasneto/ventas/transferencia/solicitud/SolicitudInicioSesion.java. Funcion: Define y valida los datos para iniciar sesion.
// Declara el paquete com.tiendasneto.ventas.transferencia.solicitud al que pertenece esta clase.
package com.tiendasneto.ventas.transferencia.solicitud;

// Importa jakarta.validation.constraints.NotBlank para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.NotBlank;
// Importa jakarta.validation.constraints.Size para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.Size;

// Datos de POST /api/auth/login; Jakarta valida los campos antes de autenticarlos.
// DTO: Define los datos que viajan por la API; Java genera automáticamente su constructor y métodos de acceso.
public record SolicitudInicioSesion(
        // El nombre de usuario es obligatorio y acepta hasta 60 caracteres.
        @NotBlank(message = "Escribe tu usuario.")
        // Aplica la anotación @Size a la declaración que sigue.
        @Size(max = 60, message = "El usuario es demasiado largo.")
        // Declara un valor local usado por la lógica de este bloque.
        String username,
        // La contrasena es obligatoria y se limita a 120 caracteres.
        @NotBlank(message = "Escribe tu contraseña.")
        // Aplica la anotación @Size a la declaración que sigue.
        @Size(max = 120, message = "La contraseña es demasiado larga.")
        // Declara un valor local usado por la lógica de este bloque.
        String password
// Continúa la expresión o declaración iniciada en las líneas cercanas.
) {}

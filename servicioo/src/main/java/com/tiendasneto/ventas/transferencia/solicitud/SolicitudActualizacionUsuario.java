// Archivo: transferencia/solicitud/SolicitudActualizacionUsuario.java. Funcion: Valida los cambios permitidos en una cuenta.
// Declara el paquete com.tiendasneto.ventas.transferencia.solicitud al que pertenece esta clase.
package com.tiendasneto.ventas.transferencia.solicitud;

// Importa jakarta.validation.constraints.NotBlank para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.NotBlank;
// Importa jakarta.validation.constraints.Pattern para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.Pattern;
// Importa jakarta.validation.constraints.Size para poder usar ese tipo o paquete en este archivo.
import jakarta.validation.constraints.Size;

/** Datos editables de una cuenta; una contrasena vacia conserva la actual. */
// DTO: Define los datos que viajan por la API; Java genera automáticamente su constructor y métodos de acceso.
public record SolicitudActualizacionUsuario(
        // Aplica la anotación @NotBlank a la declaración que sigue.
        @NotBlank(message = "Escribe el nombre de la persona.")
        // Aplica la anotación @Size a la declaración que sigue.
        @Size(max = 120, message = "El nombre no puede superar 120 caracteres.")
        // Declara un valor local usado por la lógica de este bloque.
        String nombre,

        // Aplica la anotación @NotBlank a la declaración que sigue.
        @NotBlank(message = "Escribe el nombre de usuario.")
        // Aplica la anotación @Pattern a la declaración que sigue.
        @Pattern(regexp = "[A-Za-z0-9._-]{3,60}", message = "Usa entre 3 y 60 letras, numeros, punto, guion o guion bajo.")
        // Declara un valor local usado por la lógica de este bloque.
        String username,

        // Aplica la anotación @NotBlank a la declaración que sigue.
        @NotBlank(message = "Selecciona un rol.")
        // Aplica la anotación @Pattern a la declaración que sigue.
        @Pattern(regexp = "CAJERO|LIDER|ENCARGADO", message = "Selecciona un rol valido.")
        // Declara un valor local usado por la lógica de este bloque.
        String rol,

        // Declara un valor local usado por la lógica de este bloque.
        boolean activo,

        // Aplica la anotación @Pattern a la declaración que sigue.
        @Pattern(regexp = "^$|.{10,72}", message = "La nueva contrasena debe tener entre 10 y 72 caracteres.")
        // Declara un valor local usado por la lógica de este bloque.
        String nuevaContrasena
// Continúa la expresión o declaración iniciada en las líneas cercanas.
) {}

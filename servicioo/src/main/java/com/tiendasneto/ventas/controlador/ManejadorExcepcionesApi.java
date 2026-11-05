// Archivo: src/main/java/com/tiendasneto/ventas/controlador/ManejadorExcepcionesApi.java. Funcion: Convierte errores de la API en respuestas claras para el frontend.
// Declara el paquete com.tiendasneto.ventas.controlador al que pertenece esta clase.
package com.tiendasneto.ventas.controlador;

// Importa org.springframework.http.HttpStatus para poder usar ese tipo o paquete en este archivo.
import org.springframework.http.HttpStatus;
// Importa org.springframework.http.ResponseEntity para poder usar ese tipo o paquete en este archivo.
import org.springframework.http.ResponseEntity;
// Importa org.springframework.web.bind.MethodArgumentNotValidException para poder usar ese tipo o paquete en este archivo.
import org.springframework.web.bind.MethodArgumentNotValidException;
// Importa org.springframework.web.bind.annotation.ExceptionHandler para poder usar ese tipo o paquete en este archivo.
import org.springframework.web.bind.annotation.ExceptionHandler;
// Importa org.springframework.web.bind.annotation.RestControllerAdvice para poder usar ese tipo o paquete en este archivo.
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Importa java.util.LinkedHashMap para poder usar ese tipo o paquete en este archivo.
import java.util.LinkedHashMap;
// Importa java.util.Map para poder usar ese tipo o paquete en este archivo.
import java.util.Map;

// Aplica la anotación @RestControllerAdvice a la declaración que sigue.
@RestControllerAdvice
// Declara class ManejadorExcepcionesApi y define su bloque de implementación.
public class ManejadorExcepcionesApi {
    ///////// Aplica la anotación @ExceptionHandler a la declaración que sigue.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    // Función: Convierte errores de validación en una respuesta JSON entendible.
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException exception) {
        // Declara un valor local usado por la lógica de este bloque.
        Map<String, String> errores = new LinkedHashMap<>();
        // Continúa la expresión o declaración iniciada en las líneas cercanas.
        exception.getBindingResult().getFieldErrors().forEach(error ->
                // Ejecuta esta instrucción de la lógica actual.
                errores.putIfAbsent(error.getField(), error.getDefaultMessage()));
        // Devuelve el resultado calculado por este método o bloque.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .body(Map.of("mensaje", "Revisa los datos enviados.", "errores", errores));
    // Cierra el bloque o la expresión que se definió anteriormente.
    }
// Cierra el bloque o la expresión que se definió anteriormente.
}

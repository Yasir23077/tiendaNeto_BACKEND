// Archivo: src/main/java/com/tiendasneto/ventas/AplicacionVentasNeto.java. Funcion: Arranca Spring Boot y el servidor del backend.
/*
 * Archivo: AplicacionVentasNeto.java
 * Capa: Configuración / punto de entrada
 * Descripción: Clase principal que arranca el servidor (Tomcat) en el puerto 8080.
 * Origen del código: Generado por Spring Initializr (https://start.spring.io); sin cambios
 *   del autor salvo estos comentarios.
 */

// Paquete del proyecto. Spring busca controladores y entidades en este paquete y sus subpaquetes.
// Declara el paquete com.tiendasneto.ventas al que pertenece esta clase.
package com.tiendasneto.ventas;

// SpringApplication: clase que inicia la aplicación.
// Importa org.springframework.boot.SpringApplication para poder usar ese tipo o paquete en este archivo.
import org.springframework.boot.SpringApplication;
// SpringBootApplication: anotación que activa la configuración automática de Spring Boot.
// Importa org.springframework.boot.autoconfigure.SpringBootApplication para poder usar ese tipo o paquete en este archivo.
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal. @SpringBootApplication combina configuración, autoconfiguración
 * y escaneo de componentes (detecta el controlador, el repositorio y la entidad).
 */
// Aplica la anotación @SpringBootApplication a la declaración que sigue.
@SpringBootApplication
// Declara class AplicacionVentasNeto y define su bloque de implementación.
public class AplicacionVentasNeto {

    /**
     * Método main: punto de entrada de Java.
     * @param args parámetro (variable local) con los argumentos de la línea de comandos (no se usan).
     */
    // Función: Arranca Spring Boot y el servidor web.
    public static void main(String[] args) {
        // Continúa la expresión o declaración iniciada en las líneas cercanas.
        SpringApplication.run(AplicacionVentasNeto.class, args);   // Arranca Spring y levanta el servidor web.
    // Cierra el bloque o la expresión que se definió anteriormente.
    }
// Cierra el bloque o la expresión que se definió anteriormente.
}

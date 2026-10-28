/*
 * Archivo: ServiciooApplication.java
 * Capa: Configuración / punto de entrada
 * Descripción: Clase principal que arranca el servidor (Tomcat) en el puerto 8080.
 * Origen del código: Generado por Spring Initializr (https://start.spring.io); sin cambios
 *   del autor salvo estos comentarios.
 */

// Paquete del proyecto. Spring busca controladores y entidades en este paquete y sus subpaquetes.
package com.universidad.servicioo;

// SpringApplication: clase que inicia la aplicación.
import org.springframework.boot.SpringApplication;
// SpringBootApplication: anotación que activa la configuración automática de Spring Boot.
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal. @SpringBootApplication combina configuración, autoconfiguración
 * y escaneo de componentes (detecta el controlador, el repositorio y la entidad).
 */
@SpringBootApplication
public class ServiciooApplication {

    /**
     * Método main: punto de entrada de Java.
     * @param args parámetro (variable local) con los argumentos de la línea de comandos (no se usan).
     */
    public static void main(String[] args) {
        SpringApplication.run(ServiciooApplication.class, args);   // Arranca Spring y levanta el servidor web.
    }
}
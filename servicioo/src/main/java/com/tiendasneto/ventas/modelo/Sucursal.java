// Archivo: src/main/java/com/tiendasneto/ventas/modelo/Sucursal.java. Funcion: Representa una sucursal guardada en la base de datos.
// Declara el paquete com.tiendasneto.ventas.modelo al que pertenece esta clase.
package com.tiendasneto.ventas.modelo;

// Modelo persistente de sucursal; sus ventas se identifican por el nombre.
// Importa jakarta.persistence.* para poder usar ese tipo o paquete en este archivo.
import jakarta.persistence.*;

// Aplica la anotación @Entity a la declaración que sigue.
@Entity
// Aplica la anotación @Table a la declaración que sigue.
@Table(name = "sucursales")
// Declara class Sucursal y define su bloque de implementación.
public class Sucursal {
    // Aplica la anotación @Id a la declaración que sigue.
    @Id
    // Aplica la anotación @GeneratedValue a la declaración que sigue.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private Long id;

    // Aplica la anotación @Column a la declaración que sigue.
    @Column(nullable = false, unique = true, length = 30)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private String codigo;

    // Aplica la anotación @Column a la declaración que sigue.
    @Column(nullable = false, length = 140)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private String nombre;

    // Aplica la anotación @Column a la declaración que sigue.
    @Column(nullable = false)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private boolean activa = true;

    // Función: Construye una sucursal inicial con su código y nombre.
    public Sucursal() {}
    // Función: Devuelve el valor de identificador para que otra parte del programa pueda consultarlo.
    public Long getId() { return id; }
    // Función: Asigna o actualiza el valor de identificador dentro del objeto.
    public void setId(Long id) { this.id = id; }
    // Función: Devuelve el valor de código para que otra parte del programa pueda consultarlo.
    public String getCodigo() { return codigo; }
    // Función: Asigna o actualiza el valor de código dentro del objeto.
    public void setCodigo(String codigo) { this.codigo = codigo; }
    // Función: Devuelve el valor de nombre para que otra parte del programa pueda consultarlo.
    public String getNombre() { return nombre; }
    // Función: Asigna o actualiza el valor de nombre dentro del objeto.
    public void setNombre(String nombre) { this.nombre = nombre; }
    // Función: Devuelve el valor de estado activo para que otra parte del programa pueda consultarlo.
    public boolean isActiva() { return activa; }
    // Función: Asigna o actualiza el valor de estado activo dentro del objeto.
    public void setActiva(boolean activa) { this.activa = activa; }
// Cierra el bloque o la expresión que se definió anteriormente.
}

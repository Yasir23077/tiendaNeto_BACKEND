// Archivo: src/main/java/com/tiendasneto/ventas/modelo/Producto.java. Funcion: Representa un producto guardado en la base de datos.
// Declara el paquete com.tiendasneto.ventas.modelo al que pertenece esta clase.
package com.tiendasneto.ventas.modelo;

// Modelo persistente de un producto disponible en el catálogo de la tienda.
// Importa jakarta.persistence.* para poder usar ese tipo o paquete en este archivo.
import jakarta.persistence.*;
// Importa java.math.BigDecimal para poder usar ese tipo o paquete en este archivo.
import java.math.BigDecimal;

// Aplica la anotación @Entity a la declaración que sigue.
@Entity
// Aplica la anotación @Table a la declaración que sigue.
@Table(name = "productos")
// Declara class Producto y define su bloque de implementación.
public class Producto {
    // Aplica la anotación @Id a la declaración que sigue.
    @Id
    // Aplica la anotación @GeneratedValue a la declaración que sigue.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private Long id;

    // Aplica la anotación @Column a la declaración que sigue.
    @Column(nullable = false, unique = true, length = 40)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private String codigo;

    // Aplica la anotación @Column a la declaración que sigue.
    @Column(nullable = false, unique = true, length = 160)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private String nombre;

    // Aplica la anotación @Column a la declaración que sigue.
    @Column(length = 80)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private String categoria;

    // Aplica la anotación @Column a la declaración que sigue.
    @Column(nullable = false, precision = 10, scale = 2)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private BigDecimal precio;

    // Aplica la anotación @Column a la declaración que sigue.
    @Column(nullable = false)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private boolean activo = true;

    // Función: Construye un producto inicial con sus datos de catálogo.
    public Producto() {}
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
    // Función: Devuelve el valor de categoría para que otra parte del programa pueda consultarlo.
    public String getCategoria() { return categoria; }
    // Función: Asigna o actualiza el valor de categoría dentro del objeto.
    public void setCategoria(String categoria) { this.categoria = categoria; }
    // Función: Devuelve el valor de precio para que otra parte del programa pueda consultarlo.
    public BigDecimal getPrecio() { return precio; }
    // Función: Asigna o actualiza el valor de precio dentro del objeto.
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    // Función: Devuelve el valor de estado activo para que otra parte del programa pueda consultarlo.
    public boolean isActivo() { return activo; }
    // Función: Asigna o actualiza el valor de estado activo dentro del objeto.
    public void setActivo(boolean activo) { this.activo = activo; }
// Cierra el bloque o la expresión que se definió anteriormente.
}

// Archivo: src/main/java/com/tiendasneto/ventas/modelo/Venta.java. Funcion: Representa una venta con precio, total y sucursal.
// Declara el paquete com.tiendasneto.ventas.modelo al que pertenece esta clase.
package com.tiendasneto.ventas.modelo;

// Modelo persistente de una operación de venta y sus datos financieros.
// Importa jakarta.persistence.* para poder usar ese tipo o paquete en este archivo.
import jakarta.persistence.*;
// Importa java.math.BigDecimal para poder usar ese tipo o paquete en este archivo.
import java.math.BigDecimal;
// Importa java.time.LocalDateTime para poder usar ese tipo o paquete en este archivo.
import java.time.LocalDateTime;

// Aplica la anotación @Entity a la declaración que sigue.
@Entity
// Aplica la anotación @Table a la declaración que sigue.
@Table(name = "ventas")
// Declara class Venta y define su bloque de implementación.
public class Venta {
    // Aplica la anotación @Id a la declaración que sigue.
    @Id
    // Aplica la anotación @GeneratedValue a la declaración que sigue.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private Long id;

    // Comparte el mismo numero entre los articulos comprados en un solo cobro.
    @Column(length = 32)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private String folio;

    // Aplica la anotación @Column a la declaración que sigue.
    @Column(nullable = false)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private String producto;

    // Aplica la anotación @Column a la declaración que sigue.
    @Column(nullable = false)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private Integer cantidad;

    // Aplica la anotación @Column a la declaración que sigue.
    @Column(nullable = false, precision = 10, scale = 2)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private BigDecimal precioUnitario;

    // Aplica la anotación @Column a la declaración que sigue.
    @Column(nullable = false, precision = 10, scale = 2)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private BigDecimal total;

    // Aplica la anotación @Column a la declaración que sigue.
    @Column(nullable = false)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private LocalDateTime fecha;

    // Aplica la anotación @Column a la declaración que sigue.
    @Column(length = 140)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private String sucursal;

    // Constructor vacío que JPA necesita para reconstruir la entidad al leerla de MySQL.
    public Venta() {}

    // Completa la fecha solo cuando la venta aún no tiene una.
    @PrePersist
    // Continúa la expresión o declaración iniciada en las líneas cercanas.
    void asignarFecha() {
        // Controla el flujo de ejecución según esta condición o situación.
        if (fecha == null) fecha = LocalDateTime.now();
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Getters y setters exponen las propiedades para JPA y la conversión JSON.
    // Función: Devuelve el valor de identificador para que otra parte del programa pueda consultarlo.
    public Long getId() { return id; }
    // Función: Asigna o actualiza el valor de identificador dentro del objeto.
    public void setId(Long id) { this.id = id; }
    // Función: Devuelve el valor de folio para que otra parte del programa pueda consultarlo.
    public String getFolio() { return folio; }
    // Función: Asigna o actualiza el valor de folio dentro del objeto.
    public void setFolio(String folio) { this.folio = folio; }
    // Función: Devuelve el valor de producto para que otra parte del programa pueda consultarlo.
    public String getProducto() { return producto; }
    // Función: Asigna o actualiza el valor de producto dentro del objeto.
    public void setProducto(String producto) { this.producto = producto; }
    // Función: Devuelve el valor de cantidad para que otra parte del programa pueda consultarlo.
    public Integer getCantidad() { return cantidad; }
    // Función: Asigna o actualiza el valor de cantidad dentro del objeto.
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    // Función: Devuelve el valor de precio unitario para que otra parte del programa pueda consultarlo.
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    // Función: Asigna o actualiza el valor de precio unitario dentro del objeto.
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    // Función: Devuelve el valor de total para que otra parte del programa pueda consultarlo.
    public BigDecimal getTotal() { return total; }
    // Función: Asigna o actualiza el valor de total dentro del objeto.
    public void setTotal(BigDecimal total) { this.total = total; }
    // Función: Devuelve el valor de fecha para que otra parte del programa pueda consultarlo.
    public LocalDateTime getFecha() { return fecha; }
    // Función: Asigna o actualiza el valor de fecha dentro del objeto.
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    // Función: Devuelve el valor de sucursal para que otra parte del programa pueda consultarlo.
    public String getSucursal() { return sucursal; }
    // Función: Asigna o actualiza el valor de sucursal dentro del objeto.
    public void setSucursal(String sucursal) { this.sucursal = sucursal; }
// Cierra el bloque o la expresión que se definió anteriormente.
}

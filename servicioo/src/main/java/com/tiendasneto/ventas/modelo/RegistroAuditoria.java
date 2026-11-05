// Archivo: src/main/java/com/tiendasneto/ventas/modelo/RegistroAuditoria.java. Funcion: Representa una accion guardada en la bitacora.
// Declara el paquete com.tiendasneto.ventas.modelo al que pertenece esta clase.
package com.tiendasneto.ventas.modelo;

// Modelo de un evento de auditoría: usuario, operación, entidad y contexto.
// Importa jakarta.persistence.* para poder usar ese tipo o paquete en este archivo.
import jakarta.persistence.*;
// Importa java.time.LocalDateTime para poder usar ese tipo o paquete en este archivo.
import java.time.LocalDateTime;

// Aplica la anotación @Entity a la declaración que sigue.
@Entity
// Aplica la anotación @Table a la declaración que sigue.
@Table(name = "auditoria")
// Declara class RegistroAuditoria y define su bloque de implementación.
public class RegistroAuditoria {
    // Aplica la anotación @Id a la declaración que sigue.
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private Long id;
    // Aplica la anotación @Column a la declaración que sigue.
    @Column(nullable = false) private LocalDateTime fecha;
    // Aplica la anotación @Column a la declaración que sigue.
    @Column(nullable = false, length = 120) private String usuario;
    // Aplica la anotación @Column a la declaración que sigue.
    @Column(nullable = false, length = 40) private String rol;
    // Aplica la anotación @Column a la declaración que sigue.
    @Column(nullable = false, length = 40) private String accion;
    // Aplica la anotación @Column a la declaración que sigue.
    @Column(nullable = false, length = 60) private String entidad;
    // Declara un atributo o constante disponible en el ámbito indicado.
    private Long entidadId;
    // Aplica la anotación @Column a la declaración que sigue.
    @Column(length = 500) private String detalle;
    // Aplica la anotación @Column a la declaración que sigue.
    @Column(length = 140) private String sucursal;

    // Constructor vacío que JPA necesita para reconstruir la entidad al leerla de MySQL.
    public RegistroAuditoria() {}
    // Asigna la hora del servidor al crear el evento si aún no tiene fecha.
    @PrePersist void asignarFecha() { if (fecha == null) fecha = LocalDateTime.now(); }
    // Getters y setters permiten persistir y serializar la bitácora como JSON.
    // Función: Devuelve el valor de identificador para que otra parte del programa pueda consultarlo.
    public Long getId() { return id; }
    // Función: Devuelve el valor de fecha para que otra parte del programa pueda consultarlo.
    public LocalDateTime getFecha() { return fecha; }
    // Función: Devuelve el valor de usuario para que otra parte del programa pueda consultarlo.
    public String getUsuario() { return usuario; }
    // Función: Devuelve el valor de rol para que otra parte del programa pueda consultarlo.
    public String getRol() { return rol; }
    // Función: Devuelve el valor de acción para que otra parte del programa pueda consultarlo.
    public String getAccion() { return accion; }
    // Función: Devuelve el valor de entidad para que otra parte del programa pueda consultarlo.
    public String getEntidad() { return entidad; }
    // Función: Devuelve el valor de identificador de entidad para que otra parte del programa pueda consultarlo.
    public Long getEntidadId() { return entidadId; }
    // Función: Devuelve el valor de detalle para que otra parte del programa pueda consultarlo.
    public String getDetalle() { return detalle; }
    // Función: Devuelve el valor de sucursal para que otra parte del programa pueda consultarlo.
    public String getSucursal() { return sucursal; }
    // Función: Asigna o actualiza el valor de usuario dentro del objeto.
    public void setUsuario(String v) { usuario = v; }
    // Función: Asigna o actualiza el valor de rol dentro del objeto.
    public void setRol(String v) { rol = v; }
    // Función: Asigna o actualiza el valor de acción dentro del objeto.
    public void setAccion(String v) { accion = v; }
    // Función: Asigna o actualiza el valor de entidad dentro del objeto.
    public void setEntidad(String v) { entidad = v; }
    // Función: Asigna o actualiza el valor de identificador de entidad dentro del objeto.
    public void setEntidadId(Long v) { entidadId = v; }
    // Función: Asigna o actualiza el valor de detalle dentro del objeto.
    public void setDetalle(String v) { detalle = v; }
    // Función: Asigna o actualiza el valor de sucursal dentro del objeto.
    public void setSucursal(String v) { sucursal = v; }
// Cierra el bloque o la expresión que se definió anteriormente.
}

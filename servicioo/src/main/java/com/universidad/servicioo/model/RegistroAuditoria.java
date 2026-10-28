package com.universidad.servicioo.model;

// Modelo de un evento de auditoría: usuario, operación, entidad y contexto.
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
public class RegistroAuditoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private LocalDateTime fecha;
    @Column(nullable = false, length = 120) private String usuario;
    @Column(nullable = false, length = 40) private String rol;
    @Column(nullable = false, length = 40) private String accion;
    @Column(nullable = false, length = 60) private String entidad;
    private Long entidadId;
    @Column(length = 500) private String detalle;
    @Column(length = 140) private String sucursal;

    public RegistroAuditoria() {}
    // Asigna la hora del servidor al crear el evento si aún no tiene fecha.
    @PrePersist void asignarFecha() { if (fecha == null) fecha = LocalDateTime.now(); }
    // Getters y setters permiten persistir y serializar la bitácora como JSON.
    public Long getId() { return id; }
    public LocalDateTime getFecha() { return fecha; }
    public String getUsuario() { return usuario; }
    public String getRol() { return rol; }
    public String getAccion() { return accion; }
    public String getEntidad() { return entidad; }
    public Long getEntidadId() { return entidadId; }
    public String getDetalle() { return detalle; }
    public String getSucursal() { return sucursal; }
    public void setUsuario(String v) { usuario = v; }
    public void setRol(String v) { rol = v; }
    public void setAccion(String v) { accion = v; }
    public void setEntidad(String v) { entidad = v; }
    public void setEntidadId(Long v) { entidadId = v; }
    public void setDetalle(String v) { detalle = v; }
    public void setSucursal(String v) { sucursal = v; }
}

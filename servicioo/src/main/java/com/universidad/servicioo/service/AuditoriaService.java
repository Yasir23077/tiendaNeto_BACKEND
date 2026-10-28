package com.universidad.servicioo.service;
import com.universidad.servicioo.model.RegistroAuditoria;
import com.universidad.servicioo.repository.AuditoriaRepository;

// Servicio que centraliza la creación y normalización de eventos de auditoría.
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {
    private final AuditoriaRepository repo;
    public AuditoriaService(AuditoriaRepository repo) { this.repo = repo; }

    // Guarda quién hizo qué, sobre qué registro, cuándo y en qué sucursal.
    public void registrar(String usuario, String rol, String accion, String entidad, Long id, String detalle, String sucursal) {
        RegistroAuditoria r = new RegistroAuditoria();
        r.setUsuario(limitar(usuario == null || usuario.isBlank() ? "Usuario desconocido" : usuario, 120));
        r.setRol(limitar(rol == null || rol.isBlank() ? "sin rol" : rol, 40));
        r.setAccion(limitar(accion, 40)); r.setEntidad(limitar(entidad, 60)); r.setEntidadId(id);
        r.setDetalle(limitar(detalle, 500)); r.setSucursal(limitar(sucursal, 140));
        repo.save(r);
    }
    // Evita que los textos excedan el tamaño permitido por las columnas MySQL.
    private String limitar(String valor, int max) { return valor == null ? null : valor.substring(0, Math.min(valor.length(), max)); }
}

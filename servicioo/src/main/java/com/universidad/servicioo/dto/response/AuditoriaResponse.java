package com.universidad.servicioo.dto.response;

import com.universidad.servicioo.model.RegistroAuditoria;
import java.time.LocalDateTime;

/** Campos de auditoría visibles en el historial administrativo. */
public record AuditoriaResponse(Long id, LocalDateTime fecha, String usuario, String rol,
                                String accion, String entidad, Long entidadId, String detalle, String sucursal) {
    public static AuditoriaResponse desde(RegistroAuditoria registro) {
        return new AuditoriaResponse(registro.getId(), registro.getFecha(), registro.getUsuario(), registro.getRol(),
                registro.getAccion(), registro.getEntidad(), registro.getEntidadId(), registro.getDetalle(), registro.getSucursal());
    }
}

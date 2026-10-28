package com.universidad.servicioo.repository;
import com.universidad.servicioo.model.RegistroAuditoria;

// Persistencia de los eventos de auditoría y consulta de los últimos 500.
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditoriaRepository extends JpaRepository<RegistroAuditoria, Long> {
    // Ordena de más reciente a más antiguo y limita el historial a 500 eventos.
    List<RegistroAuditoria> findTop500ByOrderByFechaDescIdDesc();
}

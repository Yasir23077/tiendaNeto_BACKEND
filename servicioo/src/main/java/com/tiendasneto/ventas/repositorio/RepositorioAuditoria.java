// Archivo: src/main/java/com/tiendasneto/ventas/repositorio/RepositorioAuditoria.java. Funcion: Consulta y guarda los registros de auditoria.
// Declara el paquete com.tiendasneto.ventas.repositorio al que pertenece esta clase.
package com.tiendasneto.ventas.repositorio;
// Importa com.tiendasneto.ventas.modelo.RegistroAuditoria para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.RegistroAuditoria;

// Persistencia de los eventos de auditoría y consulta de los últimos 500.
// Importa org.springframework.data.jpa.repository.JpaRepository para poder usar ese tipo o paquete en este archivo.
import org.springframework.data.jpa.repository.JpaRepository;
// Importa java.util.List para poder usar ese tipo o paquete en este archivo.
import java.util.List;

// Declara interface RepositorioAuditoria y define su bloque de implementación.
public interface RepositorioAuditoria extends JpaRepository<RegistroAuditoria, Long> {
    // Ordena de más reciente a más antiguo y limita el historial a 500 eventos.
    List<RegistroAuditoria> findTop500ByOrderByFechaDescIdDesc();
// Cierra el bloque o la expresión que se definió anteriormente.
}

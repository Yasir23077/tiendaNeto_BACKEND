package com.universidad.servicioo.repository;
import com.universidad.servicioo.model.Sucursal;

// Consultas CRUD y búsquedas derivadas para las sucursales.
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    // Lista las sucursales habilitadas en orden alfabético.
    List<Sucursal> findByActivaTrueOrderByNombreAsc();
    // Valida que el código no esté repetido.
    boolean existsByCodigoIgnoreCase(String codigo);
}

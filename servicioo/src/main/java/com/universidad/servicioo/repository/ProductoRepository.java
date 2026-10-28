package com.universidad.servicioo.repository;
import com.universidad.servicioo.model.Producto;

// Consultas derivadas por nombre: catálogo activo, validación de códigos y precio vigente.
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Devuelve los productos activos ordenados para mostrar sugerencias en ventas.
    List<Producto> findByActivoTrueOrderByNombreAsc();
    // Comprueba si un código ya está registrado, ignorando mayúsculas.
    boolean existsByCodigoIgnoreCase(String codigo);
    // Comprueba duplicados al editar y excluye el producto que se está modificando.
    boolean existsByCodigoIgnoreCaseAndIdNot(String codigo, Long id);
    // Busca el precio oficial por nombre para calcular una venta.
    Optional<Producto> findFirstByNombreIgnoreCaseAndActivoTrue(String nombre);
}

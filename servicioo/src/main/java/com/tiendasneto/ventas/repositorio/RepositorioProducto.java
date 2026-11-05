// Archivo: src/main/java/com/tiendasneto/ventas/repositorio/RepositorioProducto.java. Funcion: Consulta y guarda productos con Spring Data JPA.
// Declara el paquete com.tiendasneto.ventas.repositorio al que pertenece esta clase.
package com.tiendasneto.ventas.repositorio;
// Importa com.tiendasneto.ventas.modelo.Producto para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.Producto;

// Consultas derivadas por nombre: catálogo activo, validación de códigos y precio vigente.
// Importa org.springframework.data.jpa.repository.JpaRepository para poder usar ese tipo o paquete en este archivo.
import org.springframework.data.jpa.repository.JpaRepository;
// Importa java.util.List para poder usar ese tipo o paquete en este archivo.
import java.util.List;
// Importa java.util.Optional para poder usar ese tipo o paquete en este archivo.
import java.util.Optional;

// Declara interface RepositorioProducto y define su bloque de implementación.
public interface RepositorioProducto extends JpaRepository<Producto, Long> {
    // Devuelve los productos activos ordenados para mostrar sugerencias en ventas.
    List<Producto> findByActivoTrueOrderByNombreAsc();
    // Comprueba si un código ya está registrado, ignorando mayúsculas.
    boolean existsByCodigoIgnoreCase(String codigo);
    // Comprueba duplicados al editar y excluye el producto que se está modificando.
    boolean existsByCodigoIgnoreCaseAndIdNot(String codigo, Long id);
    // Busca el precio oficial por nombre para calcular una venta.
    Optional<Producto> findFirstByNombreIgnoreCaseAndActivoTrue(String nombre);
// Cierra el bloque o la expresión que se definió anteriormente.
}

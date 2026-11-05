// Archivo: src/main/java/com/tiendasneto/ventas/repositorio/RepositorioSucursal.java. Funcion: Consulta y guarda sucursales con Spring Data JPA.
// Declara el paquete com.tiendasneto.ventas.repositorio al que pertenece esta clase.
package com.tiendasneto.ventas.repositorio;
// Importa com.tiendasneto.ventas.modelo.Sucursal para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.Sucursal;

// Consultas CRUD y búsquedas derivadas para las sucursales.
// Importa org.springframework.data.jpa.repository.JpaRepository para poder usar ese tipo o paquete en este archivo.
import org.springframework.data.jpa.repository.JpaRepository;
// Importa java.util.List para poder usar ese tipo o paquete en este archivo.
import java.util.List;

// Declara interface RepositorioSucursal y define su bloque de implementación.
public interface RepositorioSucursal extends JpaRepository<Sucursal, Long> {
    // Lista las sucursales habilitadas en orden alfabético.
    List<Sucursal> findByActivaTrueOrderByNombreAsc();
    // Valida que el código no esté repetido.
    boolean existsByCodigoIgnoreCase(String codigo);
// Cierra el bloque o la expresión que se definió anteriormente.
}

// Archivo: src/main/java/com/tiendasneto/ventas/repositorio/RepositorioVenta.java. Funcion: Consulta y guarda ventas con Spring Data JPA.
// Declara el paquete com.tiendasneto.ventas.repositorio al que pertenece esta clase.
package com.tiendasneto.ventas.repositorio;
// Importa com.tiendasneto.ventas.modelo.Venta para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.Venta;

// Repositorio CRUD de ventas; JpaRepository implementa las operaciones sin SQL manual.
// Importa org.springframework.data.jpa.repository.JpaRepository para poder usar ese tipo o paquete en este archivo.
import org.springframework.data.jpa.repository.JpaRepository;

// Declara interface RepositorioVenta y define su bloque de implementación.
public interface RepositorioVenta extends JpaRepository<Venta, Long> {}

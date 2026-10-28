package com.universidad.servicioo.config;
import com.universidad.servicioo.model.Producto;
import com.universidad.servicioo.model.Sucursal;
import com.universidad.servicioo.repository.ProductoRepository;
import com.universidad.servicioo.repository.SucursalRepository;

// Datos demostrativos: se insertan solo si las tablas de productos o sucursales están vacías.
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;

@Component
public class CatalogoInicial implements CommandLineRunner {
    private final ProductoRepository productos;
    private final SucursalRepository sucursales;

    public CatalogoInicial(ProductoRepository productos, SucursalRepository sucursales) {
        this.productos = productos;
        this.sucursales = sucursales;
    }

    // Punto de inicialización que Spring ejecuta después de arrancar la aplicación.
    @Override
    public void run(String... args) {
        if (productos.count() == 0) {
            productos.saveAll(List.of(
                    producto("DEMO-001", "Arroz 1 kg", "Abarrotes", "29.90"),
                    producto("DEMO-002", "Frijol 1 kg", "Abarrotes", "34.50"),
                    producto("DEMO-003", "Leche 1 L", "Lácteos", "27.00"),
                    producto("DEMO-004", "Aceite 1 L", "Abarrotes", "42.90"),
                    producto("DEMO-005", "Agua 1.5 L", "Bebidas", "15.00"),
                    producto("DEMO-006", "Papel higiénico paquete", "Hogar", "39.90")
            ));
        }
        if (sucursales.count() == 0) {
            sucursales.saveAll(List.of(
                    sucursal("SUC-001", "Sucursal Centro"),
                    sucursal("SUC-002", "Sucursal Norte")
            ));
        }
    }

    // Construye un producto de ejemplo y convierte su precio decimal con precisión.
    private Producto producto(String codigo, String nombre, String categoria, String precio) {
        Producto producto = new Producto();
        producto.setCodigo(codigo);
        producto.setNombre(nombre);
        producto.setCategoria(categoria);
        producto.setPrecio(new BigDecimal(precio));
        producto.setActivo(true);
        return producto;
    }

    // Construye una sucursal activa de ejemplo.
    private Sucursal sucursal(String codigo, String nombre) {
        Sucursal sucursal = new Sucursal();
        sucursal.setCodigo(codigo);
        sucursal.setNombre(nombre);
        sucursal.setActiva(true);
        return sucursal;
    }
}

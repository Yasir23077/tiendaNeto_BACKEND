// Archivo: src/main/java/com/tiendasneto/ventas/configuracion/CatalogoInicial.java. Funcion: Agrega productos y sucursales de ejemplo si los catalogos estan vacios.
// Declara el paquete com.tiendasneto.ventas.configuracion al que pertenece esta clase.
package com.tiendasneto.ventas.configuracion;
// Importa com.tiendasneto.ventas.modelo.Producto para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.Producto;
// Importa com.tiendasneto.ventas.modelo.Sucursal para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.Sucursal;
// Importa com.tiendasneto.ventas.repositorio.RepositorioProducto para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.repositorio.RepositorioProducto;
// Importa com.tiendasneto.ventas.repositorio.RepositorioSucursal para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.repositorio.RepositorioSucursal;

// Datos demostrativos: se insertan solo si las tablas de productos o sucursales están vacías.
// Importa org.springframework.boot.CommandLineRunner para poder usar ese tipo o paquete en este archivo.
import org.springframework.boot.CommandLineRunner;
// Importa org.springframework.stereotype.Component para poder usar ese tipo o paquete en este archivo.
import org.springframework.stereotype.Component;
// Importa java.math.BigDecimal para poder usar ese tipo o paquete en este archivo.
import java.math.BigDecimal;
// Importa java.util.List para poder usar ese tipo o paquete en este archivo.
import java.util.List;

// Aplica la anotación @Component a la declaración que sigue.
@Component
// Declara class CatalogoInicial y define su bloque de implementación.
public class CatalogoInicial implements CommandLineRunner {
    // Declara un atributo o constante disponible en el ámbito indicado.
    private final RepositorioProducto productos;
    // Declara un atributo o constante disponible en el ámbito indicado.
    private final RepositorioSucursal sucursales;

    // Función: Constructor: crea un objeto CatalogoInicial con las dependencias que necesita.
    public CatalogoInicial(RepositorioProducto productos, RepositorioSucursal sucursales) {
        // Asigna o consulta el atributo de esta instancia.
        this.productos = productos;
        // Asigna o consulta el atributo de esta instancia.
        this.sucursales = sucursales;
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Punto de inicialización que Spring ejecuta después de arrancar la aplicación.
    @Override
    // Función: Spring ejecuta este método al iniciar para cargar datos iniciales.
    public void run(String... args) {
        // Controla el flujo de ejecución según esta condición o situación.
        if (productos.count() == 0) {
            // Continúa la expresión o declaración iniciada en las líneas cercanas.
            productos.saveAll(List.of(
                    // Continúa la expresión o declaración iniciada en las líneas cercanas.
                    producto("DEMO-001", "Arroz 1 kg", "Abarrotes", "29.90"),
                    // Continúa la expresión o declaración iniciada en las líneas cercanas.
                    producto("DEMO-002", "Frijol 1 kg", "Abarrotes", "34.50"),
                    // Continúa la expresión o declaración iniciada en las líneas cercanas.
                    producto("DEMO-003", "Leche 1 L", "Lácteos", "27.00"),
                    // Continúa la expresión o declaración iniciada en las líneas cercanas.
                    producto("DEMO-004", "Aceite 1 L", "Abarrotes", "42.90"),
                    // Continúa la expresión o declaración iniciada en las líneas cercanas.
                    producto("DEMO-005", "Agua 1.5 L", "Bebidas", "15.00"),
                    // Continúa la expresión o declaración iniciada en las líneas cercanas.
                    producto("DEMO-006", "Papel higiénico paquete", "Hogar", "39.90")
            // Ejecuta esta instrucción de la lógica actual.
            ));
        // Cierra el bloque o la expresión que se definió anteriormente.
        }
        // Controla el flujo de ejecución según esta condición o situación.
        if (sucursales.count() == 0) {
            // Continúa la expresión o declaración iniciada en las líneas cercanas.
            sucursales.saveAll(List.of(
                    // Continúa la expresión o declaración iniciada en las líneas cercanas.
                    sucursal("SUC-001", "Sucursal Centro"),
                    // Continúa la expresión o declaración iniciada en las líneas cercanas.
                    sucursal("SUC-002", "Sucursal Norte")
            // Ejecuta esta instrucción de la lógica actual.
            ));
        // Cierra el bloque o la expresión que se definió anteriormente.
        }
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Construye un producto de ejemplo y convierte su precio decimal con precisión.
    // Función: Construye un producto inicial con sus datos de catálogo.
    private Producto producto(String codigo, String nombre, String categoria, String precio) {
        // Declara un valor local usado por la lógica de este bloque.
        Producto producto = new Producto();
        // Ejecuta esta instrucción de la lógica actual.
        producto.setCodigo(codigo);
        // Ejecuta esta instrucción de la lógica actual.
        producto.setNombre(nombre);
        // Ejecuta esta instrucción de la lógica actual.
        producto.setCategoria(categoria);
        // Ejecuta esta instrucción de la lógica actual.
        producto.setPrecio(new BigDecimal(precio));
        // Ejecuta esta instrucción de la lógica actual.
        producto.setActivo(true);
        // Devuelve el resultado calculado por este método o bloque.
        return producto;
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Construye una sucursal activa de ejemplo.
    // Función: Construye una sucursal inicial con su código y nombre.
    private Sucursal sucursal(String codigo, String nombre) {
        // Declara un valor local usado por la lógica de este bloque.
        Sucursal sucursal = new Sucursal();
        // Ejecuta esta instrucción de la lógica actual.
        sucursal.setCodigo(codigo);
        // Ejecuta esta instrucción de la lógica actual.
        sucursal.setNombre(nombre);
        // Ejecuta esta instrucción de la lógica actual.
        sucursal.setActiva(true);
        // Devuelve el resultado calculado por este método o bloque.
        return sucursal;
    // Cierra el bloque o la expresión que se definió anteriormente.
    }
// Cierra el bloque o la expresión que se definió anteriormente.
}

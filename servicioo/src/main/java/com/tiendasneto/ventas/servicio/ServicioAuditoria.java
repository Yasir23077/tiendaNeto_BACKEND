// Archivo: src/main/java/com/tiendasneto/ventas/servicio/ServicioAuditoria.java. Funcion: Centraliza el registro de acciones en la bitacora.
// Declara el paquete com.tiendasneto.ventas.servicio al que pertenece esta clase.
package com.tiendasneto.ventas.servicio;
// Importa com.tiendasneto.ventas.modelo.RegistroAuditoria para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.modelo.RegistroAuditoria;
// Importa com.tiendasneto.ventas.repositorio.RepositorioAuditoria para poder usar ese tipo o paquete en este archivo.
import com.tiendasneto.ventas.repositorio.RepositorioAuditoria;

// Servicio que centraliza la creación y normalización de eventos de auditoría.
// Importa org.springframework.stereotype.Service para poder usar ese tipo o paquete en este archivo.
import org.springframework.stereotype.Service;

// Aplica la anotación @Service a la declaración que sigue.
@Service
// Declara class ServicioAuditoria y define su bloque de implementación.
public class ServicioAuditoria {
    // Declara un atributo o constante disponible en el ámbito indicado.
    private final RepositorioAuditoria repo;
    // Función: Constructor: crea un objeto ServicioAuditoria con las dependencias que necesita.
    public ServicioAuditoria(RepositorioAuditoria repo) { this.repo = repo; }

    // Guarda quién hizo qué, sobre qué registro, cuándo y en qué sucursal.
    // Función: Guarda en auditoría quién hizo qué cambio.
    public void registrar(String usuario, String rol, String accion, String entidad, Long id, String detalle, String sucursal) {
        // Ejecuta esta instrucción de la lógica actual.
        RegistroAuditoria r = new RegistroAuditoria();
        // Ejecuta esta instrucción de la lógica actual.
        r.setUsuario(limitar(usuario == null || usuario.isBlank() ? "Usuario desconocido" : usuario, 120));
        // Ejecuta esta instrucción de la lógica actual.
        r.setRol(limitar(rol == null || rol.isBlank() ? "sin rol" : rol, 40));
        // Ejecuta esta instrucción de la lógica actual.
        r.setAccion(limitar(accion, 40)); r.setEntidad(limitar(entidad, 60)); r.setEntidadId(id);
        // Ejecuta esta instrucción de la lógica actual.
        r.setDetalle(limitar(detalle, 500)); r.setSucursal(limitar(sucursal, 140));
        // Ejecuta esta instrucción de la lógica actual.
        repo.save(r);
    // Cierra el bloque o la expresión que se definió anteriormente.
    }
    // Evita que los textos excedan el tamaño permitido por las columnas MySQL.
    // Recorta el texto al máximo indicado y deja null sin cambios.
    private String limitar(String valor, int max) { return valor == null ? null : valor.substring(0, Math.min(valor.length(), max)); }
// Cierra el bloque o la expresión que se definió anteriormente.
}

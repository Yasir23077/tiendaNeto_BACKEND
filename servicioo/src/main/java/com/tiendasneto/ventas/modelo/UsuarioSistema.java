// Archivo: src/main/java/com/tiendasneto/ventas/modelo/UsuarioSistema.java. Funcion: Representa una cuenta, su rol y su estado.
// Declara el paquete com.tiendasneto.ventas.modelo al que pertenece esta clase.
package com.tiendasneto.ventas.modelo;

// Importa jakarta.persistence.* para poder usar ese tipo o paquete en este archivo.
import jakarta.persistence.*;

// Aplica la anotación @Entity a la declaración que sigue.
@Entity
// Aplica la anotación @Table a la declaración que sigue.
@Table(name = "usuarios_sistema")
// Declara class UsuarioSistema y define su bloque de implementación.
public class UsuarioSistema {
    // Llave primaria de la tabla, generada automaticamente por la base de datos.
    @Id
    // Aplica la anotación @GeneratedValue a la declaración que sigue.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private Long id;

    // Nombre de acceso obligatorio, unico y con un maximo de 60 caracteres.
    @Column(nullable = false, unique = true, length = 60)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private String username;

    // Hash codificado de la contrasena; no se guarda la clave en texto legible.
    @Column(nullable = false, length = 100)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private String passwordHash;

    // Nombre de la persona que se devuelve al frontend despues del inicio de sesion.
    @Column(nullable = false, length = 120)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private String nombre;

    // Rol de autorizacion: CAJERO, LIDER o ENCARGADO.
    @Column(nullable = false, length = 20)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private String rol;

    // Permite bloquear la cuenta sin borrar su fila de la base de datos.
    @Column(nullable = false)
    // Declara un atributo o constante disponible en el ámbito indicado.
    private boolean activo = true;

    // Constructor requerido por JPA para cargar una cuenta desde la base de datos.
    // Constructor vacío que JPA necesita para reconstruir la entidad al leerla de MySQL.
    protected UsuarioSistema() {}

    // Constructor que usa el componente de arranque para crear una cuenta.
    // Construye una cuenta con usuario, hash de contraseña, nombre y rol inicial.
    public UsuarioSistema(String username, String passwordHash, String nombre, String rol) {
        // Asigna o consulta el atributo de esta instancia.
        this.username = username;
        // Asigna o consulta el atributo de esta instancia.
        this.passwordHash = passwordHash;
        // Asigna o consulta el atributo de esta instancia.
        this.nombre = nombre;
        // Asigna o consulta el atributo de esta instancia.
        this.rol = rol;
        // Asigna o consulta el atributo de esta instancia.
        this.activo = true;
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Devuelve el identificador generado por la base de datos.
    // Función: Devuelve el valor de identificador para que otra parte del programa pueda consultarlo.
    public Long getId() { return id; }
    // Devuelve el nombre de acceso.
    // Función: Devuelve el valor de usuario para que otra parte del programa pueda consultarlo.
    public String getUsername() { return username; }
    // Función: Asigna o actualiza el valor de usuario dentro del objeto.
    public void setUsername(String username) { this.username = username; }
    // Devuelve el hash que Spring Security compara durante la autenticacion.
    // Función: Devuelve el valor de contraseña cifrada para que otra parte del programa pueda consultarlo.
    public String getPasswordHash() { return passwordHash; }
    // Declara un método o constructor y sus parámetros de entrada.
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    // Devuelve el nombre legible de la persona.
    // Función: Devuelve el valor de nombre para que otra parte del programa pueda consultarlo.
    public String getNombre() { return nombre; }
    // Función: Asigna o actualiza el valor de nombre dentro del objeto.
    public void setNombre(String nombre) { this.nombre = nombre; }
    // Devuelve el rol que verifican las reglas de autorizacion.
    // Función: Devuelve el valor de rol para que otra parte del programa pueda consultarlo.
    public String getRol() { return rol; }
    // Función: Asigna o actualiza el valor de rol dentro del objeto.
    public void setRol(String rol) { this.rol = rol; }
    // Indica si esta cuenta puede iniciar sesion.
    // Función: Devuelve el valor de estado activo para que otra parte del programa pueda consultarlo.
    public boolean isActivo() { return activo; }
    // Función: Asigna o actualiza el valor de estado activo dentro del objeto.
    public void setActivo(boolean activo) { this.activo = activo; }
// Cierra el bloque o la expresión que se definió anteriormente.
}

# Guía del código: Sistema de ventas NETO

Este documento explica la responsabilidad de cada archivo de código fuente y configuración del sistema. El frontend está en `fronted/` y el backend Spring Boot está en `servicioo/`.

La estructura Java sigue capas: `controller` recibe solicitudes HTTP; `service` concentra lógica reutilizable; `repository` consulta y persiste datos; `model` contiene las entidades; `config` agrupa la carga inicial de datos; `ServiciooApplication.java` permanece en el paquete raíz para que Spring descubra las capas descendientes.

```text
src/main/java/com/universidad/servicioo/
├── ServiciooApplication.java
├── config/CatalogoInicial.java
├── controller/{Auditoria,Producto,Sucursal,Venta}Controller.java
├── model/{Producto,RegistroAuditoria,Sucursal,Venta}.java
├── repository/{Auditoria,Producto,Sucursal,Venta}Repository.java
└── service/AuditoriaService.java
```

## Backend Spring Boot

| Archivo | Función |
| --- | --- |
| `ServiciooApplication.java` | Punto de entrada Java; inicia Spring Boot y el servidor web. |
| `model/Venta.java` | Entidad JPA de una venta: producto, cantidad, precio, total, fecha y sucursal. Asigna fecha antes de insertar si viene vacía. |
| `repository/VentaRepository.java` | Operaciones CRUD de la tabla `ventas`, proporcionadas por Spring Data JPA. |
| `controller/VentaController.java` | API `/api/ventas`: lista, busca, crea, edita y elimina ventas; valida campos, calcula el total y registra operaciones en auditoría. |
| `model/Producto.java` | Entidad JPA del catálogo: código, nombre, categoría, precio y estado activo. |
| `repository/ProductoRepository.java` | Consulta productos activos, valida códigos duplicados y busca el precio vigente por nombre. |
| `controller/ProductoController.java` | API `/api/productos`: lista productos, valida códigos y datos, permite alta y edición y registra cambios en auditoría. |
| `model/Sucursal.java` | Entidad JPA con código, nombre y estado activo de una sucursal. |
| `repository/SucursalRepository.java` | Operaciones CRUD, listado ordenado de sucursales activas y validación de códigos. |
| `controller/SucursalController.java` | API `/api/sucursales`: lista y registra sucursales, validando código único y dejando registro de auditoría. |
| `model/RegistroAuditoria.java` | Entidad persistente que guarda actor, rol, fecha, acción, entidad afectada, identificador, detalle y sucursal. |
| `repository/AuditoriaRepository.java` | Acceso a la tabla de auditoría y consulta de los 500 eventos más recientes. |
| `service/AuditoriaService.java` | Construye y guarda los eventos de auditoría; limita la longitud de texto para respetar el esquema. |
| `controller/AuditoriaController.java` | API `GET /api/auditoria`; devuelve el historial al rol encargado y responde 403 a otros roles. |
| `config/CatalogoInicial.java` | Inserta datos demostrativos de productos y sucursales solo cuando las tablas están vacías. |
| `ServiciooApplicationTests.java` | Prueba de contexto de Spring que comprueba que la aplicación puede inicializarse. |
| `application.properties` | Configura nombre de aplicación, conexión MySQL `tienda_neto`, usuario, contraseña, actualización de tablas y SQL de depuración. |
| `pom.xml` | Define versión de Java, dependencias de Spring Web/JPA/MySQL y plugins Maven. |
| `mvnw`, `mvnw.cmd` | Maven Wrapper para ejecutar Maven en Unix o Windows usando la versión configurada. |

### Rutas del backend

| Método y ruta | Acción |
| --- | --- |
| `GET /api/ventas` | Consultar ventas. |
| `GET /api/ventas/{id}` | Consultar una venta por folio. |
| `POST /api/ventas` | Registrar venta y evento `CREAR`. |
| `PUT /api/ventas/{id}` | Editar venta y evento `ACTUALIZAR`. |
| `DELETE /api/ventas/{id}` | Eliminar venta y evento `ELIMINAR`. |
| `GET /api/productos` | Consultar productos activos. |
| `POST /api/productos` / `PUT /api/productos/{id}` | Crear o actualizar producto y auditar el cambio. |
| `GET /api/sucursales` / `POST /api/sucursales` | Consultar o crear sucursales. |
| `GET /api/auditoria` | Consultar los últimos eventos como encargado. |

## Frontend React y Vite

| Archivo | Función |
| --- | --- |
| `src/main.jsx` | Monta React en `#root`; importa Bootstrap, estilos globales y `App`. |
| `fronted/src/presentation/App.jsx` | Estado principal de sesión, navegación, ventas, catálogos, métricas y carga de auditoría; conecta componentes con los servicios HTTP. |
| `fronted/src/presentation/components/Login.jsx` | Formulario visual de acceso y validación de credenciales de demostración. |
| `fronted/src/presentation/components/NetoLogo.jsx` | Dibuja el logotipo NETO con texto y estilos CSS, claro u oscuro. |
| `fronted/src/presentation/components/FormularioVenta.jsx` | Captura sucursal, producto y cantidad; sugiere productos, autocompleta precio y calcula el total. |
| `fronted/src/presentation/components/TablaVentas.jsx` | Presenta el historial de ventas y muestra botones permitidos según el rol. |
| `fronted/src/presentation/components/GestionCatalogos.jsx` | Permite consultar productos y sucursales; el encargado puede agregar/editar productos y agregar sucursales. |
| `fronted/src/presentation/components/TablaAuditoria.jsx` | Muestra los eventos, fecha, actor, acción y detalle; implementa la búsqueda local. |
| `fronted/src/models/venta.js` | Normaliza datos de venta y calcula el total del formulario. |
| `fronted/src/models/auditoria.js` | Filtra eventos y presenta fechas en formato local. |
| `fronted/src/services/autenticacionService.js` | Solicita sesiones, login, logout y perfil al backend protegido por Spring Security. |
| `fronted/src/services/ventaService.js` | Funciones HTTP para listar, crear, editar y eliminar ventas; transforma errores HTTP en excepciones. |
| `fronted/src/services/catalogoService.js` | Funciones HTTP para consultar y modificar productos y sucursales. |
| `fronted/src/services/auditoriaService.js` | Solicita la bitácora al backend y presenta errores de autorización o servidor. |
| `fronted/src/services/sesionHeaders.js` | Prepara los encabezados JSON y el token CSRF para solicitudes de escritura. |
| `fronted/src/styles/index.css` | Estilos globales del sistema, paleta NETO, paneles, formularios, tablas, login, catálogo y adaptación móvil. |
| `fronted/src/styles/App.css` | Estilos residuales de la plantilla Vite; la pantalla NETO se define principalmente en `index.css`. |
| `vite.config.js` | Activa React en Vite y reenvía `/api` a Spring en `localhost:8080`. |
| `index.html` | Página HTML inicial y contenedor donde se monta React. |
| `package.json` | Dependencias y comandos de desarrollo/compilación del frontend. |
| `public/favicon.svg`, `public/icons.svg` | Recursos SVG estáticos de icono e interfaz. |
| `src/assets/hero.png` | Imagen estática usada por la interfaz si el componente correspondiente la referencia. |
| `src/assets/react.svg`, `src/assets/vite.svg` | Recursos heredados de la plantilla; no participan en las operaciones de ventas. |

## Reglas principales

### DTO y validación de solicitudes

La API utiliza DTO de entrada (dto/request) para especificar qué campos puede enviar el navegador y DTO de salida (dto/response) para definir qué campos devuelve el backend. Las entidades JPA permanecen dentro de la capa de persistencia y ya no se exponen directamente en estos endpoints.

@Valid activa Jakarta Bean Validation en los cuerpos de creación y actualización. @NotBlank, @NotNull, @Positive, @Size, @DecimalMin y @Digits validan obligatoriedad, rangos y longitudes. ApiExceptionHandler.java reúne los errores de campo y responde HTTP 400 con un mensaje que el frontend presenta al usuario. Las reglas que dependen de la base, como códigos duplicados, siguen comprobándose en los controladores.

- Una venta debe incluir producto, cantidad mayor que cero, precio no negativo y sucursal.
- El backend vuelve a tomar el precio del catálogo cuando reconoce el producto y calcula el total como precio por cantidad.
- Los códigos de productos y sucursales deben ser únicos.
- Los productos y sucursales inactivos no se muestran en los listados operativos.
- La bitácora registra altas, cambios y eliminaciones de ventas, altas/cambios de productos y altas de sucursales.
- La tabla de auditoría se actualiza automáticamente con `spring.jpa.hibernate.ddl-auto=update` al arrancar Spring.
- Spring Security valida la sesión en el servidor y aplica permisos por rol. El navegador ya no determina la identidad ni envía encabezados de rol.

## Cómo ejecutar

1. Iniciar el servidor MySQL/MariaDB y verificar la conexión definida en `application.properties`.
2. En `servicioo/`, ejecutar `mvn spring-boot:run`.
3. En `fronted/`, ejecutar `npm run dev`.
4. Abrir `http://localhost:5173/`.

## Seguridad de acceso

La autenticación se realiza en el backend con Spring Security. El navegador envía credenciales a POST /api/auth/login; Spring busca la cuenta en usuarios_sistema, compara su contraseña con BCrypt y crea una sesión de servidor. GET /api/auth/me recupera el perfil y POST /api/auth/logout invalida la sesión.

SecurityConfig exige sesión para las rutas de la API y restringe las operaciones por rol: cualquier usuario autenticado consulta y registra ventas; líder y encargado pueden actualizar ventas; solo encargado puede eliminar ventas, modificar catálogos y consultar auditoría. La identidad de auditoría se obtiene de la sesión autenticada, nunca de encabezados enviados por el navegador.

Las operaciones POST, PUT y DELETE requieren el token CSRF de Spring. La sesión se guarda en una cookie HttpOnly; el token CSRF se expone en una cookie legible para que React lo envíe en el encabezado X-XSRF-TOKEN. Vite comunica el frontend con Spring mediante el proxy /api.

En desarrollo se crean las cuentas cajero, lider y encargado si no existen. Las contraseñas iniciales se pueden configurar mediante NETO_CAJERO_PASSWORD, NETO_LIDER_PASSWORD y NETO_ENCARGADO_PASSWORD; la configuración solo se aplica al crear cada cuenta. Desactiva la creación de cuentas demo con NETO_DEMO_USERS_ENABLED=false en entornos que no deban usarlas.

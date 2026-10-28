# Estructura del frontend NETO Ventas

El frontend se organiza por responsabilidades. La interfaz vive en `presentation`, los datos y reglas compartidas en `models`, las llamadas HTTP en `services`, la sesión de demostración en `auth` y los estilos en `styles`.

```text
src/
├── main.jsx                         # Arranque de React
├── models/                          # Formatos y reglas del dominio
│   ├── venta.js
│   └── auditoria.js
├── presentation/                    # Interfaz de usuario
│   ├── App.jsx
│   └── components/
│       ├── FormularioVenta.jsx
│       ├── GestionCatalogos.jsx
│       ├── Login.jsx
│       ├── NetoLogo.jsx
│       ├── TablaAuditoria.jsx
│       └── TablaVentas.jsx
├── services/                        # Comunicación con el backend
│   ├── auditoriaService.js
│   ├── catalogoService.js
│   ├── sesionHeaders.js
│   └── ventaService.js
├── auth/demoUsers.js                # Perfiles de demostración
├── styles/                          # Estilos globales y de plantilla
│   ├── index.css
│   └── App.css
└── assets/                          # Imágenes incluidas en el bundle
```

## Responsabilidad de cada archivo

| Archivo | Función |
| --- | --- |
| `src/main.jsx` | Crea la raíz de React, activa `StrictMode` e importa Bootstrap y estilos. |
| `src/presentation/App.jsx` | Controla la sesión, navegación por pestañas, permisos visuales, carga de datos, acciones de venta y métricas. |
| `src/presentation/components/Login.jsx` | Renderiza el acceso y comunica el usuario validado al componente padre. |
| `src/presentation/components/FormularioVenta.jsx` | Captura sucursal, producto y cantidad; usa el modelo para completar precio y total. |
| `src/presentation/components/GestionCatalogos.jsx` | Presenta formularios y tablas de productos y sucursales. |
| `src/presentation/components/TablaVentas.jsx` | Renderiza el historial de operaciones y acciones permitidas. |
| `src/presentation/components/TablaAuditoria.jsx` | Renderiza y busca eventos del registro de auditoría. |
| `src/presentation/components/NetoLogo.jsx` | Dibuja el nombre y los detalles del logotipo NETO. |
| `src/models/venta.js` | Normaliza los valores del formulario y calcula el total de una venta. |
| `src/models/auditoria.js` | Filtra eventos y convierte sus fechas a formato local de México. |
| `src/services/ventaService.js` | Ejecuta las solicitudes GET/POST/PUT/DELETE de ventas y procesa errores. |
| `src/services/catalogoService.js` | Ejecuta las solicitudes de productos y sucursales. |
| `src/services/auditoriaService.js` | Consulta la bitácora y traduce las respuestas HTTP de error. |
| `src/services/sesionHeaders.js` | Lee `sessionStorage` y prepara nombre/rol para registrar quién realizó una mutación. |
| `src/auth/demoUsers.js` | Define usuarios demostrativos y busca coincidencias de usuario/contraseña. |
| `src/styles/index.css` | Define colores, tipografía, componentes visuales, paneles y reglas adaptables. |
| `src/styles/App.css` | Conserva estilos de plantilla Vite que no son parte central de la pantalla. |
| `src/assets/hero.png` | Imagen del proyecto, disponible para importación desde componentes. |
| `src/assets/react.svg`, `src/assets/vite.svg` | Imágenes heredadas de la plantilla inicial. |
| `public/favicon.svg`, `public/icons.svg` | Recursos estáticos servidos directamente por Vite. |
| `vite.config.js` | Configura React y el proxy `/api` hacia Spring Boot en `localhost:8080`. |
| `index.html` | Documento HTML que aporta el elemento `#root` para React. |
| `package.json` | Declara dependencias y comandos npm. |

## Comentarios en el código

Los archivos de lógica incluyen comentarios junto a sus funciones y estados principales. En JSX los comentarios se ubican por secciones visuales con `{/* ... */}`. En CSS se usan comentarios por grupo de selectores; comentar cada propiedad CSS individual repetiría lo que ya expresa su nombre y haría difícil mantener la hoja de estilos.

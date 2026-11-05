// Cliente HTTP para consultar y mantener productos y sucursales.
import { headersSesion } from './encabezadosSesion'
import { datosErrorApi } from './errorApi'
// Valida una respuesta del backend y extrae su contenido JSON.
// Función: Comprueba el estado HTTP y convierte errores en excepciones.
async function respuestaJson(respuesta) {
  if (!respuesta.ok) {
    const detalle = await datosErrorApi(respuesta)
    const error = new Error(detalle.mensaje)
    error.errores = detalle.errores
    error.status = detalle.status
    throw error
  }
  return respuesta.json()
}

// Función: Consulta el catálogo activo de productos.
export async function listarProductos() {
  // Consulta el catálogo activo para sugerencias y administración.
  return respuestaJson(await fetch('/api/productos'))
}

// Consulta un producto por ID y devuelve null si el backend responde 404.
export async function obtenerProducto(id) {
  const respuesta = await fetch(`/api/productos/${encodeURIComponent(id)}`)
  if (respuesta.status === 404) return null
  return respuestaJson(respuesta)
}

// Función: Envía un producto nuevo al backend.
export async function crearProducto(datos) {
  // Registra un producto nuevo y sus valores de catálogo.
  return respuestaJson(await fetch('/api/productos', {
    method: 'POST', headers: headersSesion(true), body: JSON.stringify(datos),
  }))
}

// Función: Envía cambios del producto elegido.
export async function actualizarProducto(id, datos) {
  // Actualiza producto y precio en el catálogo.
  return respuestaJson(await fetch(`/api/productos/${id}`, {
    method: 'PUT', headers: headersSesion(true), body: JSON.stringify(datos),
  }))
}

// Función: Consulta las sucursales activas.
export async function listarSucursales() {
  // Carga las sucursales activas para los selectores del sistema.
  return respuestaJson(await fetch('/api/sucursales'))
}

// Función: Envía por POST el código y nombre de la sucursal junto con el encabezado CSRF.
export async function crearSucursal(datos) {
  // Registra una sucursal nueva y agrega actor para la bitácora.
  return respuestaJson(await fetch('/api/sucursales', {
    method: 'POST', headers: headersSesion(true), body: JSON.stringify(datos),
  }))
}

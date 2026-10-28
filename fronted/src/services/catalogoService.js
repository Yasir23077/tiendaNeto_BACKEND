// Cliente HTTP para consultar y mantener productos y sucursales.
import { headersSesion } from './sesionHeaders'
import { mensajeError } from './apiError'
// Valida una respuesta del backend y extrae su contenido JSON.
async function respuestaJson(respuesta) {
  if (!respuesta.ok) throw new Error(await mensajeError(respuesta))
  return respuesta.json()
}

export async function listarProductos() {
  // Consulta el catálogo activo para sugerencias y administración.
  return respuestaJson(await fetch('/api/productos'))
}

export async function crearProducto(datos) {
  // Registra un producto nuevo y sus valores de catálogo.
  return respuestaJson(await fetch('/api/productos', {
    method: 'POST', headers: headersSesion(true), body: JSON.stringify(datos),
  }))
}

export async function actualizarProducto(id, datos) {
  // Actualiza producto y precio en el catálogo.
  return respuestaJson(await fetch(`/api/productos/${id}`, {
    method: 'PUT', headers: headersSesion(true), body: JSON.stringify(datos),
  }))
}

export async function listarSucursales() {
  // Carga las sucursales activas para los selectores del sistema.
  return respuestaJson(await fetch('/api/sucursales'))
}

export async function crearSucursal(datos) {
  // Registra una sucursal nueva y agrega actor para la bitácora.
  return respuestaJson(await fetch('/api/sucursales', {
    method: 'POST', headers: headersSesion(true), body: JSON.stringify(datos),
  }))
}

// Cliente HTTP para las operaciones REST de ventas.
import { headersSesion } from './sesionHeaders'
import { mensajeError } from './apiError'
const API_URL = '/api/ventas'

// Convierte respuestas HTTP en JSON o errores entendibles por la interfaz.
async function manejarRespuesta(respuesta) {
  if (!respuesta.ok) {
    const detalle = await mensajeError(respuesta)
    throw new Error(detalle)
  }
  return respuesta.status === 204 ? null : respuesta.json()
}

export async function listarVentas() {
  // GET devuelve el historial completo para las tablas y métricas.
  return manejarRespuesta(await fetch(API_URL))
}

export async function crearVenta(datos) {
  // POST envía una venta nueva junto con el actor de la sesión actual.
  return manejarRespuesta(await fetch(API_URL, {
    method: 'POST', headers: headersSesion(true), body: JSON.stringify(datos),
  }))
}

export async function actualizarVenta(id, datos) {
  // PUT reemplaza los datos del folio indicado.
  return manejarRespuesta(await fetch(`${API_URL}/${id}`, {
    method: 'PUT', headers: headersSesion(true), body: JSON.stringify(datos),
  }))
}

export async function eliminarVenta(id) {
  // DELETE elimina un folio; el backend guarda antes su evento de auditoría.
  return manejarRespuesta(await fetch(`${API_URL}/${id}`, { method: 'DELETE', headers: headersSesion() }))
}

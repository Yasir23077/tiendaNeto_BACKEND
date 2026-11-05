// Cliente HTTP para las operaciones REST de ventas.
import { headersSesion } from './encabezadosSesion'
import { mensajeError } from './errorApi'
const API_URL = '/api/ventas'

// Convierte respuestas HTTP en JSON o errores entendibles por la interfaz.
// Función: Interpreta la respuesta JSON y comunica errores HTTP.
async function manejarRespuesta(respuesta) {
  // Evita intentar leer como datos correctos una respuesta fallida del servidor.
  if (!respuesta.ok) {
    const detalle = await mensajeError(respuesta)
    throw new Error(detalle)
  }
  // Las respuestas 204 no tienen cuerpo; las demás contienen el resultado JSON.
  return respuesta.status === 204 ? null : respuesta.json()
}

// Función: Solicita el historial de ventas por GET.
export async function listarVentas() {
  // GET devuelve el historial completo para las tablas y métricas.
  return manejarRespuesta(await fetch(API_URL))
}

// Función: Registra una venta individual por POST.
export async function crearVenta(datos) {
  // POST envía una venta nueva junto con el actor de la sesión actual.
  return manejarRespuesta(await fetch(API_URL, {
    method: 'POST', headers: headersSesion(true), body: JSON.stringify(datos),
  }))
}

// Registra todos los productos de un cliente en una sola solicitud y bajo un folio.
// Función: Registra varias líneas de una compra por POST.
export async function crearVentaLote(datos) {
  return manejarRespuesta(await fetch(`${API_URL}/lote`, {
    method: 'POST', headers: headersSesion(true), body: JSON.stringify(datos),
  }))
}

// Función: Actualiza una venta por PUT.
export async function actualizarVenta(id, datos) {
  // PUT reemplaza los datos del folio indicado.
  return manejarRespuesta(await fetch(`${API_URL}/${id}`, {
    method: 'PUT', headers: headersSesion(true), body: JSON.stringify(datos),
  }))
}

// Función: Elimina una venta por DELETE.
export async function eliminarVenta(id) {
  // DELETE elimina un folio; el backend guarda antes su evento de auditoría.
  return manejarRespuesta(await fetch(`${API_URL}/${id}`, { method: 'DELETE', headers: headersSesion() }))
}

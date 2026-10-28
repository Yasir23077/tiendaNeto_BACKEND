// Convierte errores estructurados de la API en mensajes breves para la interfaz.
export async function mensajeError(respuesta) {
  if (respuesta.status === 401 && typeof window !== 'undefined') {
    window.dispatchEvent(new Event('neto:sesion-expirada'))
  }
  const contenido = await respuesta.text()
  let datos = {}
  try {
    datos = contenido ? JSON.parse(contenido) : {}
  } catch { /* Puede llegar un error de texto simple. */ }
  const campos = datos.errores ? Object.values(datos.errores).join(' ') : ''
  return campos || datos.mensaje || contenido || (respuesta.status === 403
    ? 'No tienes permiso para realizar esta acción.'
    : respuesta.status === 400 ? 'Revisa los datos enviados.' : `Error ${respuesta.status}`)
}

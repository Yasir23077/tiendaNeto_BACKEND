// Convierte errores estructurados de la API en mensajes breves para la interfaz.
// Función: Lee la respuesta fallida y crea un mensaje sencillo.
export async function mensajeError(respuesta) {
  // Si el backend responde 401, avisa a la app para volver a mostrar el login.
  if (respuesta.status === 401 && typeof window !== 'undefined') {
    window.dispatchEvent(new Event('neto:sesion-expirada'))
  }
  // Lee el cuerpo una sola vez para convertirlo a un mensaje utilizable.
  const contenido = await respuesta.text()
  // Prepara un resultado vacio si la API no devuelve JSON.
  let datos = {}
  try {
    datos = contenido ? JSON.parse(contenido) : {}
  } catch { /* Puede llegar un error de texto simple. */ }
  // Junta errores de validacion de campos si el backend los envio.
  const campos = datos.errores ? Object.values(datos.errores).join(' ') : ''
  // Prioriza errores de campo o mensaje del backend; ofrece alternativa por codigo HTTP.
  // Devuelve el primer mensaje disponible, desde el detalle de campo hasta el estado HTTP.
  return campos || datos.mensaje || contenido || (respuesta.status === 403
    ? 'No tienes permiso para realizar esta acción.'
    : respuesta.status === 400 ? 'Revisa los datos enviados.' : `Error ${respuesta.status}`)
}

// Lee también el mapa de errores para que el formulario los muestre junto a cada campo.
export async function datosErrorApi(respuesta) {
  if (respuesta.status === 401 && typeof window !== 'undefined') {
    window.dispatchEvent(new Event('neto:sesion-expirada'))
  }
  const contenido = await respuesta.text()
  let datos = {}
  try {
    datos = contenido ? JSON.parse(contenido) : {}
  } catch { /* La respuesta puede ser texto plano. */ }
  const errores = datos.errores && typeof datos.errores === 'object' ? datos.errores : {}
  const campos = Object.values(errores).join(' ')
  const mensaje = campos || datos.mensaje || contenido || (respuesta.status === 403
    ? 'No tienes permiso para realizar esta acción.'
    : respuesta.status === 400 ? 'Revisa los datos enviados.' : `Error ${respuesta.status}`)
  return { mensaje, errores, status: respuesta.status }
}

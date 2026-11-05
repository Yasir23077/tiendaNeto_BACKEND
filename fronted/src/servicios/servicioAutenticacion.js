// Acceso y sesión administrados por Spring Security con cookie de sesión.
// Conserva el token CSRF mientras el frontend usa la sesion HTTP de Spring.
import { tokenCsrfActual } from './encabezadosSesion'

let csrfToken = null
// Encabezado que el backend espera para comprobar el token CSRF.
let csrfHeader = 'X-XSRF-TOKEN'

// Solicita al backend un token antes de iniciar sesion o modificar datos.
// Función: Solicita el token para proteger operaciones que cambian datos.
async function obtenerCsrf() {
  // Pide un token y permite que el navegador envie las cookies del mismo origen.
  const response = await fetch('/api/auth/csrf', { credentials: 'same-origin' })
  if (!response.ok) throw new Error('No se pudo iniciar la protección de la sesión.')
  // Lee el nombre del encabezado y el token generados por Spring Security.
  const datos = await response.json()
  // Usa el valor crudo de la cookie que valida la configuracion CSRF para SPA.
  csrfToken = tokenCsrfActual() || datos.token
  csrfHeader = datos.headerName
}

// Procesa JSON de respuestas y transforma estados HTTP de error en excepciones.
// Función: Interpreta la respuesta y lanza un error entendible si falla.
async function jsonSeguro(response) {
  // Lee el cuerpo como texto para soportar respuestas vacias.
  const body = await response.text()
  // Intenta convertir el texto a JSON; usa objeto vacio si no es JSON valido.
  let datos = {}
  try { datos = body ? JSON.parse(body) : {} } catch { datos = {} }
  // Propaga el mensaje del backend o el codigo HTTP cuando algo falla.
  if (!response.ok) throw new Error(datos.mensaje || `Error ${response.status}`)
  // Devuelve el contenido procesado de una respuesta correcta.
  return datos
}

// Obtiene CSRF y envia las credenciales al endpoint de login.
// Función: Guarda el perfil de usuario confirmado por el servidor.
export async function iniciarSesion(datos) {
  await obtenerCsrf()
  // Spring valida el login, crea la sesion y devuelve el perfil del usuario.
  const response = await fetch('/api/auth/login', {
    method: 'POST',
    credentials: 'same-origin',
    headers: { 'Content-Type': 'application/json', [csrfHeader]: csrfToken },
    body: JSON.stringify(datos),
  })
  // Lee el perfil autenticado o muestra el error de credenciales.
  const perfil = await jsonSeguro(response)
  // Spring renueva el token CSRF al autenticar; obtiene el nuevo para operaciones posteriores.
  await obtenerCsrf()
  return perfil
}

// Consulta si la cookie de sesion corresponde a una sesion autenticada.
// Función: Comprueba si el usuario mantiene una sesión activa.
export async function obtenerSesion() {
  await obtenerCsrf()
  const response = await fetch('/api/auth/me', { credentials: 'same-origin' })
  // 401 significa que la persona no ha iniciado sesion o su sesion expiro.
  if (response.status === 401) return null
  return jsonSeguro(response)
}

// Solicita al backend cerrar e invalidar la sesion actual.
// Función: Solicita cerrar sesión y limpia el estado de la interfaz.
export async function cerrarSesion() {
  if (!csrfToken) await obtenerCsrf()
  // Envia cookie de sesion y token CSRF para proteger esta operacion POST.
  const response = await fetch('/api/auth/logout', {
    method: 'POST',
    credentials: 'same-origin',
    headers: { [csrfHeader]: csrfToken },
  })
  if (response.status !== 204 && !response.ok) await jsonSeguro(response)
  // Elimina el token en memoria para no reutilizarlo despues del cierre.
  csrfToken = null
}

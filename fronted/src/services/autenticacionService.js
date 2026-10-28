// Acceso y sesión administrados por Spring Security con cookie de sesión.
let csrfToken = null
let csrfHeader = 'X-XSRF-TOKEN'

async function obtenerCsrf() {
  const response = await fetch('/api/auth/csrf', { credentials: 'same-origin' })
  if (!response.ok) throw new Error('No se pudo iniciar la protección de la sesión.')
  const datos = await response.json()
  csrfToken = datos.token
  csrfHeader = datos.headerName
}

async function jsonSeguro(response) {
  const body = await response.text()
  let datos = {}
  try { datos = body ? JSON.parse(body) : {} } catch { datos = {} }
  if (!response.ok) throw new Error(datos.mensaje || `Error ${response.status}`)
  return datos
}

export async function iniciarSesion(datos) {
  await obtenerCsrf()
  const response = await fetch('/api/auth/login', {
    method: 'POST',
    credentials: 'same-origin',
    headers: { 'Content-Type': 'application/json', [csrfHeader]: csrfToken },
    body: JSON.stringify(datos),
  })
  return jsonSeguro(response)
}

export async function obtenerSesion() {
  await obtenerCsrf()
  const response = await fetch('/api/auth/me', { credentials: 'same-origin' })
  if (response.status === 401) return null
  return jsonSeguro(response)
}

export async function cerrarSesion() {
  if (!csrfToken) await obtenerCsrf()
  const response = await fetch('/api/auth/logout', {
    method: 'POST',
    credentials: 'same-origin',
    headers: { [csrfHeader]: csrfToken },
  })
  if (response.status !== 204 && !response.ok) await jsonSeguro(response)
  csrfToken = null
}

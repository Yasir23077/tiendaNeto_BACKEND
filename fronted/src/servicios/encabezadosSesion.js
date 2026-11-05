// Añade JSON y el token CSRF emitido por Spring Security a solicitudes de escritura.
// Función: Lee y decodifica la cookie CSRF.
export function tokenCsrfActual() {
  // Obtiene las cookies que JavaScript tiene permitido leer.
  const cookies = document.cookie.split(';').map((cookie) => cookie.trim())
  // Encuentra la cookie XSRF-TOKEN creada por Spring Security.
  const cookieCsrf = cookies.find((cookie) => cookie.startsWith('XSRF-TOKEN='))
  // Usa un token vacio cuando la cookie todavia no existe.
  let tokenCsrf = ''
  // Decodifica el valor de la cookie; si esta mal formado, conserva token vacio.
  try { tokenCsrf = cookieCsrf ? decodeURIComponent(cookieCsrf.slice('XSRF-TOKEN='.length)) : '' } catch { tokenCsrf = '' }
  return tokenCsrf
}

// Función: Construye encabezados HTTP con JSON o CSRF cuando son necesarios.
export function headersSesion(json = false) {
  // Recupera el token actual porque Spring exige CSRF en operaciones que modifican datos.
  const tokenCsrf = tokenCsrfActual()
  // Devuelve Content-Type para JSON y el encabezado CSRF para Spring.
  return {
    ...(json ? { 'Content-Type': 'application/json' } : {}),
    ...(tokenCsrf ? { 'X-XSRF-TOKEN': tokenCsrf } : {}),
  }
}

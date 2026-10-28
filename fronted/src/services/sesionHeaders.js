// Añade JSON y el token CSRF emitido por Spring Security a solicitudes de escritura.
export function headersSesion(json = false) {
  const cookies = document.cookie.split(';').map((cookie) => cookie.trim())
  const cookieCsrf = cookies.find((cookie) => cookie.startsWith('XSRF-TOKEN='))
  let tokenCsrf = ''
  try { tokenCsrf = cookieCsrf ? decodeURIComponent(cookieCsrf.slice('XSRF-TOKEN='.length)) : '' } catch { tokenCsrf = '' }
  return {
    ...(json ? { 'Content-Type': 'application/json' } : {}),
    ...(tokenCsrf ? { 'X-XSRF-TOKEN': tokenCsrf } : {}),
  }
}

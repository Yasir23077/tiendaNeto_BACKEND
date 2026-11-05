// Cliente HTTP para la administracion de cuentas del sistema.
import { headersSesion } from './encabezadosSesion'
import { mensajeError } from './errorApi'

// Valida la respuesta y convierte errores de la API en mensajes legibles.
// Función: Comprueba el estado HTTP y convierte errores en excepciones.
async function respuestaJson(respuesta) {
  if (!respuesta.ok) throw new Error(await mensajeError(respuesta))
  return respuesta.json()
}

// Consulta usuarios; el backend permite esta operacion solo al encargado.
// Función: Consulta cuentas para la administración.
export async function listarUsuarios() {
  return respuestaJson(await fetch('/api/usuarios', { headers: headersSesion() }))
}

// Solicita la creacion de una cuenta y envia CSRF en el encabezado.
// Función: Envía al backend una cuenta nueva.
export async function crearUsuario(datos) {
  return respuestaJson(await fetch('/api/usuarios', {
    method: 'POST',
    headers: headersSesion(true),
    body: JSON.stringify(datos),
  }))
}


// Modifica una cuenta existente; el backend limita esta operacion al encargado.
// Función: Envía cambios de la cuenta elegida.
export async function actualizarUsuario(id, datos) {
  return respuestaJson(await fetch(`/api/usuarios/${id}`, {
    method: 'PUT',
    headers: headersSesion(true),
    body: JSON.stringify(datos),
  }))
}

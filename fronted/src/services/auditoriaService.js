// Cliente HTTP para consultar el historial protegido de auditoría.
import { headersSesion } from './sesionHeaders'
import { mensajeError } from './apiError'

// Solicita la bitácora; el backend permite leerla solo al rol encargado.
export async function listarAuditoria() {
  const respuesta = await fetch('/api/auditoria', { headers: headersSesion() })
  if (!respuesta.ok) throw new Error(await mensajeError(respuesta))
  return respuesta.json()
}

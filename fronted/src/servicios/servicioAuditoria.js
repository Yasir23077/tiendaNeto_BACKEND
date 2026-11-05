// Cliente HTTP para consultar el historial protegido de auditoría.
import { headersSesion } from './encabezadosSesion'
import { mensajeError } from './errorApi'

// Solicita la bitácora; el backend permite leerla solo al rol encargado.
// Función: Consulta la bitácora protegida del sistema.
export async function listarAuditoria() {
  // El proxy de Vite envía esta ruta al backend y conserva el mismo origen para la sesión.
  const respuesta = await fetch('/api/auditoria', { headers: headersSesion() })
  // Convierte los errores HTTP a mensajes que la interfaz puede mostrar.
  if (!respuesta.ok) throw new Error(await mensajeError(respuesta))
  // Devuelve los registros recibidos en formato JSON.
  return respuesta.json()
}

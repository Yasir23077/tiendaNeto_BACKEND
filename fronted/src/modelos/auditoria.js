/**
 * Funciones del dominio de auditoría, independientes de la interfaz visual.
 */

/** Busca un término en los datos visibles de cada evento de auditoría. */
// Función: Busca texto en los campos visibles de auditoría.
export function filtrarRegistrosAuditoria(registros, busqueda) {
  // Normaliza el término para que la búsqueda no dependa de mayúsculas o acentos locales.
  const termino = busqueda.trim().toLocaleLowerCase('es-MX')
  // Si el campo está vacío, no hace falta recorrer ni filtrar la lista.
  if (!termino) return registros

  // Conserva cada evento cuando alguno de sus valores visibles contiene el término.
  return registros.filter((registro) => [
    registro.usuario,
    registro.rol,
    registro.accion,
    registro.entidad,
    registro.detalle,
    registro.sucursal,
    registro.entidadId,
  // Convierte valores nulos y numéricos a texto para compararlos de forma segura.
  ].some((valor) => String(valor ?? '').toLocaleLowerCase('es-MX').includes(termino)))
}

/** Presenta la fecha ISO del backend con fecha y hora locales de México. */
// Función: Convierte una fecha a formato local de México.
export function formatearFechaAuditoria(valor) {
  // Formatea fechas válidas para México y usa una raya cuando no hay fecha.
  return valor
    ? new Intl.DateTimeFormat('es-MX', { dateStyle: 'medium', timeStyle: 'short' }).format(new Date(valor))
    : '—'
}

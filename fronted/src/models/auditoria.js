/**
 * Funciones del dominio de auditoría, independientes de la interfaz visual.
 */

/** Busca un término en los datos visibles de cada evento de auditoría. */
export function filtrarRegistrosAuditoria(registros, busqueda) {
  const termino = busqueda.trim().toLocaleLowerCase('es-MX')
  if (!termino) return registros

  return registros.filter((registro) => [
    registro.usuario,
    registro.rol,
    registro.accion,
    registro.entidad,
    registro.detalle,
    registro.sucursal,
    registro.entidadId,
  ].some((valor) => String(valor ?? '').toLocaleLowerCase('es-MX').includes(termino)))
}

/** Presenta la fecha ISO del backend con fecha y hora locales de México. */
export function formatearFechaAuditoria(valor) {
  return valor
    ? new Intl.DateTimeFormat('es-MX', { dateStyle: 'medium', timeStyle: 'short' }).format(new Date(valor))
    : '—'
}

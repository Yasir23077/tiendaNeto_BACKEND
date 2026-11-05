// Vista de bitácora con formato local de fecha y búsqueda en los eventos recibidos.
import { useMemo, useState } from 'react'
import { filtrarRegistrosAuditoria, formatearFechaAuditoria } from '../../modelos/auditoria'
import Paginacion from './Paginacion'

// Renderiza la actividad registrada por el servidor y permite filtrar los eventos.
// Función: Componente que muestra los eventos de auditoría registrados en el backend.
export default function TablaAuditoria({ registros, cargando, error }) {
  // Mantiene el texto del filtro sin alterar los registros originales.
  const [busqueda, setBusqueda] = useState('')
  // Pagina actual que se muestra de la tabla de auditoria.
  const [pagina, setPagina] = useState(1)
  // Filtra por cualquier campo visible sin modificar la lista original.
  const filtrados = useMemo(() => {
    return filtrarRegistrosAuditoria(registros, busqueda)
  }, [registros, busqueda])
  // Calcula la cantidad de paginas y corta los resultados a diez filas.
  // Mantiene al menos una página y evita que la selección supere el nuevo total.
  const totalPaginas = Math.max(1, Math.ceil(filtrados.length / 10))
  const paginaActual = Math.min(pagina, totalPaginas)
  const filtradosPagina = filtrados.slice((paginaActual - 1) * 10, paginaActual * 10)

  // Muestra filtros, tabla, estados vacíos y paginación del historial de auditoría.
  return <section className="content-card history-card audit-panel">
    {/* Encabezado y campo de búsqueda de la bitácora. */}
    <div className="section-title audit-heading">
      <div><p className="eyebrow">CONTROL DE ACTIVIDAD</p><h2>Bitácora de auditoría</h2><p className="audit-description">Cambios recientes en ventas, productos y sucursales.</p></div>
      <label className="audit-search">Buscar<input className="form-control" value={busqueda} onChange={(e) => { setBusqueda(e.target.value); setPagina(1) }} placeholder="Usuario, acción, sucursal…" /></label>
    </div>
    {error && <div className="alert alert-danger">{error}</div>}
    {/* Tabla de eventos recientes, estado de carga y mensaje cuando no hay coincidencias. */}
    {cargando ? <div className="audit-empty">Cargando actividad…</div> : <>
      <div className="table-responsive audit-table-wrap"><table className="table align-middle audit-table">
        <thead><tr><th>Fecha y hora</th><th>Usuario</th><th>Acción</th><th>Registro</th><th>Detalle</th><th>Sucursal</th></tr></thead>
        <tbody>{filtradosPagina.map((r) => <tr key={r.id}>
          <td className="audit-date">{formatearFechaAuditoria(r.fecha)}</td>
          <td><strong className="audit-user">{r.usuario}</strong><small className="audit-role">{r.rol}</small></td>
          <td><span className={`audit-action audit-${r.accion?.toLowerCase()}`}>{r.accion}</span></td>
          <td>{r.entidad}<small className="audit-role">#{r.entidadId ?? '—'}</small></td>
          <td className="audit-detail">{r.detalle || '—'}</td><td>{r.sucursal || '—'}</td>
        </tr>)}
        {!filtrados.length && <tr><td colSpan="6" className="audit-empty">{registros.length ? 'No hay resultados para esta búsqueda.' : 'Todavía no hay acciones registradas.'}</td></tr>}
        </tbody>
      </table></div>
      <Paginacion total={filtrados.length} pagina={paginaActual} onCambiar={setPagina} etiqueta="eventos" />
      <div className="audit-footer">Coincidencias encontradas: {filtrados.length}. La API devuelve hasta 500 eventos.</div>
    </>}
  </section>
}

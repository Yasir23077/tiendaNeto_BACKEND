// Vista de bitácora con formato local de fecha y búsqueda en los eventos recibidos.
import { useMemo, useState } from 'react'
import { filtrarRegistrosAuditoria, formatearFechaAuditoria } from '../../models/auditoria'

// Renderiza la actividad registrada por el servidor y permite filtrar los eventos.
export default function TablaAuditoria({ registros, cargando, error }) {
  // Mantiene el texto del filtro sin alterar los registros originales.
  const [busqueda, setBusqueda] = useState('')
  // Filtra por cualquier campo visible sin modificar la lista original.
  const filtrados = useMemo(() => {
    return filtrarRegistrosAuditoria(registros, busqueda)
  }, [registros, busqueda])

  return <section className="content-card history-card audit-panel">
    {/* Encabezado y campo de búsqueda de la bitácora. */}
    <div className="section-title audit-heading">
      <div><p className="eyebrow">CONTROL DE ACTIVIDAD</p><h2>Bitácora de auditoría</h2><p className="audit-description">Cambios recientes en ventas, productos y sucursales.</p></div>
      <label className="audit-search">Buscar<input className="form-control" value={busqueda} onChange={(e) => setBusqueda(e.target.value)} placeholder="Usuario, acción, sucursal…" /></label>
    </div>
    {error && <div className="alert alert-danger">{error}</div>}
    {/* Tabla de eventos recientes, estado de carga y mensaje cuando no hay coincidencias. */}
    {cargando ? <div className="audit-empty">Cargando actividad…</div> : <>
      <div className="table-responsive audit-table-wrap"><table className="table align-middle audit-table">
        <thead><tr><th>Fecha y hora</th><th>Usuario</th><th>Acción</th><th>Registro</th><th>Detalle</th><th>Sucursal</th></tr></thead>
        <tbody>{filtrados.map((r) => <tr key={r.id}>
          <td className="audit-date">{formatearFechaAuditoria(r.fecha)}</td>
          <td><strong className="audit-user">{r.usuario}</strong><small className="audit-role">{r.rol}</small></td>
          <td><span className={`audit-action audit-${r.accion?.toLowerCase()}`}>{r.accion}</span></td>
          <td>{r.entidad}<small className="audit-role">#{r.entidadId ?? '—'}</small></td>
          <td className="audit-detail">{r.detalle || '—'}</td><td>{r.sucursal || '—'}</td>
        </tr>)}
        {!filtrados.length && <tr><td colSpan="6" className="audit-empty">{registros.length ? 'No hay resultados para esta búsqueda.' : 'Todavía no hay acciones registradas.'}</td></tr>}
        </tbody>
      </table></div>
      <div className="audit-footer">Mostrando {filtrados.length} de {registros.length} eventos recientes · máximo 500</div>
    </>}
  </section>
}

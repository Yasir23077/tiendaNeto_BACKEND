// Tabla del historial de ventas; muestra acciones según los permisos del perfil.
const moneda = new Intl.NumberFormat('es-MX', { style: 'currency', currency: 'MXN' })

function TablaVentas({ ventas, onEditar, onEliminar, puedeEditar, puedeEliminar, cargando = false }) {
  const mostrarAcciones = puedeEditar || puedeEliminar
  return (
    <div className="table-responsive">
      {/* Columnas de importes y acciones disponibles de acuerdo con el rol. */}
      <table className="table table-hover align-middle">
        <thead><tr><th>Folio</th><th>Fecha</th><th>Producto</th><th className="text-end">Cant.</th>
          <th className="text-end">Precio unitario</th><th className="text-end">Total</th>{mostrarAcciones && <th>Acciones</th>}</tr></thead>
        <tbody>
          {cargando && <tr><td colSpan={mostrarAcciones ? 7 : 6} className="text-center text-muted py-4"><span className="spinner-border spinner-border-sm me-2" role="status" />Cargando ventas…</td></tr>}
          {!cargando && ventas.map((venta) => (
            <tr key={venta.id}>
              <td><span className="badge text-bg-light">#{venta.id}</span></td>
              <td>{venta.fecha ? new Date(venta.fecha).toLocaleString('es-MX') : '—'}</td>
              <td className="fw-semibold">{venta.producto}</td>
              <td className="text-end">{venta.cantidad}</td>
              <td className="text-end">{moneda.format(venta.precioUnitario)}</td>
              <td className="text-end fw-bold">{moneda.format(venta.total)}</td>
              {mostrarAcciones && <td className="text-nowrap">
                {puedeEditar && <button type="button" className="btn btn-outline-dark btn-sm me-2" onClick={() => onEditar(venta)}>Editar</button>}
                {puedeEliminar && <button type="button" className="btn btn-outline-orange btn-sm" onClick={() => onEliminar(venta.id)}>Eliminar</button>}
              </td>}
            </tr>
          ))}
          {!cargando && ventas.length === 0 && <tr><td colSpan={mostrarAcciones ? 7 : 6} className="text-center text-muted py-4">No hay ventas que coincidan con la búsqueda.</td></tr>}
        </tbody>
      </table>
    </div>
  )
}

export default TablaVentas

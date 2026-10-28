// Formulario del punto de venta: sugiere productos, carga el precio y calcula total.
import { useEffect, useState } from 'react'
import { calcularTotalVenta, normalizarVenta } from '../../models/venta'

function FormularioVenta({ ventaEnEdicion, productos, sucursales, sucursalActual, onSucursalChange, onGuardar, onCancelar }) {
  // Campos controlados; si llega una venta, se inicializan con sus valores.
  const [producto, setProducto] = useState(ventaEnEdicion?.producto ?? '')
  const [cantidad, setCantidad] = useState(ventaEnEdicion?.cantidad ?? 1)
  const [precioUnitario, setPrecioUnitario] = useState(ventaEnEdicion?.precioUnitario ?? '')
  const [sucursal, setSucursal] = useState(ventaEnEdicion?.sucursal ?? sucursalActual ?? '')
  const [guardando, setGuardando] = useState(false)
  const [errorProducto, setErrorProducto] = useState('')
  const [codigoBarras, setCodigoBarras] = useState('')
  // El modelo del dominio centraliza el cálculo que se refleja en el formulario.
  // Los valores derivados no duplican estado y se actualizan con cada cambio.
  const total = calcularTotalVenta(cantidad, precioUnitario)
  const productoCoincidente = productos.find((item) =>
    item.nombre.toLocaleLowerCase() === producto.trim().toLocaleLowerCase())

  const buscarCodigoBarras = (codigo) => {
    const encontrado = productos.find((item) => item.codigo.trim().toLocaleLowerCase() === codigo.trim().toLocaleLowerCase())
    if (!encontrado) {
      setErrorProducto('No encontramos ese código en el catálogo.')
      return
    }
    setProducto(encontrado.nombre)
    setPrecioUnitario(encontrado.precio)
    setCodigoBarras('')
    setErrorProducto('')
  }

  // Selecciona una sucursal inicial cuando el formulario aún no tiene una.
  useEffect(() => {
    if (!sucursal && sucursales.length) setSucursal(sucursalActual || sucursales[0].nombre)
  }, [sucursal, sucursales, sucursalActual])

  // Sincroniza automáticamente el precio con el producto elegido del catálogo.
  useEffect(() => {
    if (productoCoincidente) setPrecioUnitario(productoCoincidente.precio)
  }, [productoCoincidente])

  // Valida mediante HTML, envía la operación y limpia campos si se creó.
  const manejarEnvio = async (evento) => {
    evento.preventDefault()
    if (!productoCoincidente) {
      setErrorProducto('Selecciona un producto del catálogo o busca por su código de barras.')
      document.getElementById('producto')?.focus()
      return
    }
    setGuardando(true)
    setErrorProducto('')
    try {
      const datos = normalizarVenta({
        producto: productoCoincidente.nombre,
        cantidad,
        precioUnitario: productoCoincidente.precio,
        sucursal,
      })
      const exito = await onGuardar(datos)
      if (exito && !ventaEnEdicion) {
        setProducto('')
        setCantidad(1)
        setPrecioUnitario('')
        setCodigoBarras('')
      }
    } finally {
      setGuardando(false)
    }
  }

  // Restaura el formulario y avisa al componente padre que cancele la edición.
  const manejarCancelar = () => {
    setProducto('')
    setCantidad(1)
    setPrecioUnitario('')
    setSucursal(sucursalActual || sucursales[0]?.nombre || '')
    onCancelar()
  }

  return (
    <form onSubmit={manejarEnvio} className="row g-3 mb-4">
      {/* Contexto de la operación: sucursal donde se registra esta venta. */}
      <div className="col-md-4">
        <label htmlFor="sucursal-venta" className="form-label">Sucursal</label>
        <select id="sucursal-venta" className="form-select" value={sucursal} onChange={(e) => { setSucursal(e.target.value); onSucursalChange(e.target.value) }} required disabled={guardando}>
          <option value="" disabled>Selecciona una sucursal</option>
          {sucursales.map((item) => <option key={item.id} value={item.nombre}>{item.codigo} · {item.nombre}</option>)}
        </select>
      </div>
      {/* Entrada con sugerencias de catálogo; elegir un artículo sincroniza su precio. */}
      <div className="col-md-8">
        <label htmlFor="producto" className="form-label">Producto</label>
        <select id="producto" className="form-select" value={productoCoincidente?.id ?? ''} required disabled={guardando || !productos.length}
          aria-invalid={Boolean(errorProducto)} aria-describedby="ayuda-producto"
          onChange={(e) => {
            const seleccionado = productos.find((item) => String(item.id) === e.target.value)
            setProducto(seleccionado?.nombre ?? '')
            setPrecioUnitario(seleccionado?.precio ?? '')
            setErrorProducto('')
          }}>
          <option value="" disabled>{productos.length ? 'Selecciona un producto' : 'No hay productos en el catálogo'}</option>
          {productos.map((item) => <option key={item.id} value={item.id}>{item.codigo} · {item.nombre} · {Number(item.precio).toLocaleString('es-MX', { style: 'currency', currency: 'MXN' })}</option>)}
        </select>
        <small id="ayuda-producto" className={`catalog-hint ${errorProducto ? 'catalog-hint-error' : ''}`}>{errorProducto || (productoCoincidente ? `Código ${productoCoincidente.codigo} · precio del catálogo` : 'El precio se completa automáticamente al elegir un producto.')}</small>
      </div>
      <div className="col-md-4">
        <label htmlFor="codigo-barras" className="form-label">Código de barras <span className="text-muted fw-normal">(opcional)</span></label>
        <input id="codigo-barras" className="form-control" value={codigoBarras} placeholder="Escanea o escribe el código"
          disabled={guardando || !productos.length} aria-label="Buscar producto por código de barras"
          onChange={(e) => {
            const valor = e.target.value
            setCodigoBarras(valor)
            setErrorProducto('')
            if (productos.some((item) => item.codigo.trim().toLocaleLowerCase() === valor.trim().toLocaleLowerCase())) buscarCodigoBarras(valor)
          }}
          onKeyDown={(e) => {
            if (e.key === 'Enter') {
              e.preventDefault()
              if (codigoBarras.trim()) buscarCodigoBarras(codigoBarras)
            }
          }} />
      </div>
      {/* Datos numéricos de la venta y total calculado por el modelo. */}
      <div className="col-sm-4 col-md-2">
        <label htmlFor="cantidad" className="form-label">Cantidad</label>
        <input id="cantidad" type="number" min="1" step="1" className="form-control" value={cantidad}
          onChange={(e) => setCantidad(e.target.value)} required disabled={guardando} />
      </div>
      <div className="col-sm-4 col-md-2">
        <label htmlFor="precio" className="form-label">Precio unitario (MXN)</label>
        <input id="precio" type="number" min="0" step="0.01" className="form-control" value={precioUnitario}
          onChange={(e) => setPrecioUnitario(e.target.value)} readOnly required disabled={guardando} />
      </div>
      <div className="col-sm-4 col-md-3">
        <label className="form-label" htmlFor="total">Total de venta</label>
        <div className="input-group"><span className="input-group-text">$</span>
          <input id="total" className="form-control" value={total} readOnly aria-label="Total calculado" />
        </div>
      </div>
      {/* Acciones principales para guardar o abandonar una edición. */}
      <div className="col-12">
        <button type="submit" className="btn btn-neto me-2" disabled={!sucursales.length || !productos.length || guardando}>
          {guardando && <span className="spinner-border spinner-border-sm me-2" aria-hidden="true" />}
          {guardando ? 'Guardando…' : ventaEnEdicion ? 'Actualizar venta' : 'Registrar venta'}
        </button>
        {ventaEnEdicion && <button type="button" className="btn btn-outline-secondary" onClick={manejarCancelar} disabled={guardando}>Cancelar</button>}
      </div>
    </form>
  )
}

export default FormularioVenta

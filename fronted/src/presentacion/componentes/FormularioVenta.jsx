// Formulario para preparar un carrito y registrar varios productos en un mismo cobro.
import { useEffect, useState } from 'react'
import { calcularTotalVenta, normalizarVenta } from '../../modelos/venta'

const moneda = new Intl.NumberFormat('es-MX', { style: 'currency', currency: 'MXN' })

// Función: Administra el formulario de venta y los artículos del carrito.
function FormularioVenta({ ventaEnEdicion, productos, sucursales, sucursalActual, onSucursalChange, onGuardar, onCancelar }) {
  const [productoId, setProductoId] = useState('')
  const [busquedaProducto, setBusquedaProducto] = useState('')
  const [cantidad, setCantidad] = useState(1)
  const [sucursal, setSucursal] = useState(ventaEnEdicion?.sucursal ?? sucursalActual ?? '')
  const [carrito, setCarrito] = useState([])
  const [guardando, setGuardando] = useState(false)
  const [errorProducto, setErrorProducto] = useState('')
  const [codigoBarras, setCodigoBarras] = useState('')

  // Recupera el producto completo para mostrar su precio y enviar su identificador.
  const productoSeleccionado = productos.find((item) => String(item.id) === String(productoId))
  // Limita las opciones usando el código, nombre o categoría escritos en la búsqueda.
  const productosFiltrados = productos.filter((item) =>
    [item.codigo, item.nombre, item.categoria].some((valor) =>
      String(valor ?? '').toLocaleLowerCase().includes(busquedaProducto.trim().toLocaleLowerCase())))
  // Suma el importe de cada artículo para mostrar el total antes de cobrar.
  const totalCarrito = carrito.reduce((suma, item) => suma + Number(item.precio) * item.cantidad, 0)
  // Recalcula el importe de la venta seleccionada cuando cambia la cantidad.
  const totalEdicion = calcularTotalVenta(cantidad, ventaEnEdicion?.precioUnitario ?? productoSeleccionado?.precio ?? 0)

  // Prepara los campos cuando un lider o encargado abre una venta para editarla.
  useEffect(() => {
    if (!ventaEnEdicion) return
    const producto = productos.find((item) => item.nombre.toLocaleLowerCase() === ventaEnEdicion.producto?.toLocaleLowerCase())
    setProductoId(producto ? String(producto.id) : '')
    setCantidad(ventaEnEdicion.cantidad)
    setSucursal(ventaEnEdicion.sucursal ?? sucursalActual ?? '')
  }, [ventaEnEdicion, productos, sucursalActual])

  // Elige automaticamente la sucursal disponible al iniciar una venta nueva.
  useEffect(() => {
    if (!sucursal && sucursales.length) setSucursal(sucursalActual || sucursales[0].nombre)
  }, [sucursal, sucursales, sucursalActual])

  // Función: Busca un producto por el código escrito o escaneado.
  const seleccionarProductoPorCodigo = (codigo) => {
    const producto = productos.find((item) => item.codigo.trim().toLocaleLowerCase() === codigo.trim().toLocaleLowerCase())
    if (!producto) {
      setErrorProducto('No encontramos ese codigo en el catalogo.')
      return
    }
    setProductoId(String(producto.id))
    setCodigoBarras('')
    setErrorProducto('')
  }

  // Agrega el articulo al carrito y suma la cantidad si ya estaba en el.
  // Función: Valida y agrega el producto o acumula su cantidad.
  const agregarAlCarrito = () => {
    if (!productoSeleccionado) {
      setErrorProducto('Selecciona un producto del catalogo.')
      return
    }
    const unidades = Number(cantidad)
    if (!Number.isInteger(unidades) || unidades < 1) {
      setErrorProducto('La cantidad debe ser un numero entero mayor que cero.')
      return
    }
    setCarrito((actual) => {
      const existente = actual.find((item) => item.productoId === productoSeleccionado.id)
      if (existente) return actual.map((item) => item.productoId === productoSeleccionado.id
        ? { ...item, cantidad: item.cantidad + unidades }
        : item)
      return [...actual, {
        productoId: productoSeleccionado.id,
        codigo: productoSeleccionado.codigo,
        nombre: productoSeleccionado.nombre,
        precio: Number(productoSeleccionado.precio),
        cantidad: unidades,
      }]
    })
    setProductoId('')
    setCantidad(1)
    setErrorProducto('')
  }

  // Envia el ticket completo; al editar, mantiene la operacion individual existente.
  // Función: Valida los datos y envía una venta o el carrito completo.
  const manejarEnvio = async (evento) => {
    evento.preventDefault()
    if (!sucursal) {
      setErrorProducto('Selecciona una sucursal antes de cobrar.')
      return
    }
    if (ventaEnEdicion) {
      if (!productoSeleccionado) {
        setErrorProducto('Selecciona un producto del catalogo.')
        return
      }
      setGuardando(true)
      try {
        const exito = await onGuardar(normalizarVenta({
          producto: productoSeleccionado.nombre,
          cantidad,
          precioUnitario: productoSeleccionado.precio,
          sucursal,
        }))
        if (exito) onCancelar()
      } finally {
        setGuardando(false)
      }
      return
    }
    if (!carrito.length) {
      setErrorProducto('Agrega al menos un producto al carrito antes de registrar la venta.')
      return
    }
    setGuardando(true)
    try {
      const exito = await onGuardar({
        sucursal,
        productos: carrito.map((item) => ({ productoId: item.productoId, cantidad: item.cantidad })),
      })
      if (exito) {
        setCarrito([])
        setProductoId('')
        setCantidad(1)
      }
    } finally {
      setGuardando(false)
    }
  }

  // Quita una linea antes de confirmar el cobro.
  // Función: Quita del carrito el producto indicado.
  const quitarDelCarrito = (productoIdQuitar) => {
    setCarrito((actual) => actual.filter((item) => item.productoId !== productoIdQuitar))
  }

  // Renderiza la captura de productos, la sucursal elegida y el ticket actual.
  return (
    <form onSubmit={manejarEnvio} className="sale-form">
      <div className="row g-3 align-items-end mb-3">
        <div className="col-md-4">
          <label htmlFor="sucursal-venta" className="form-label">Sucursal</label>
          <select id="sucursal-venta" className="form-select" value={sucursal} onChange={(e) => { setSucursal(e.target.value); onSucursalChange(e.target.value) }} required disabled={guardando}>
            <option value="" disabled>Selecciona una sucursal</option>
            {sucursales.map((item) => <option key={item.id} value={item.nombre}>{item.codigo} · {item.nombre}</option>)}
          </select>
        </div>
        <div className="col-md-8">
          <label htmlFor="buscar-producto" className="form-label">Buscar producto</label>
          <input id="buscar-producto" className="form-control mb-2" type="search" value={busquedaProducto} onChange={(e) => setBusquedaProducto(e.target.value)} placeholder="Nombre, codigo o categoria" disabled={guardando || !productos.length} />
          <select id="producto" className="form-select" value={productoId} disabled={guardando || !productosFiltrados.length} onChange={(e) => { setProductoId(e.target.value); setErrorProducto('') }}>
            <option value="">{productosFiltrados.length ? 'Selecciona un producto' : 'No hay productos coincidentes'}</option>
            {productosFiltrados.map((item) => <option key={item.id} value={item.id}>{item.codigo} · {item.nombre} · {moneda.format(item.precio)}</option>)}
          </select>
          <small className="catalog-hint">{productoSeleccionado ? `Precio del catalogo: ${moneda.format(productoSeleccionado.precio)}` : 'Al elegir un producto se muestra su precio vigente.'}</small>
        </div>
        <div className="col-md-4">
          <label htmlFor="codigo-barras" className="form-label">Codigo de barras (opcional)</label>
          <input id="codigo-barras" className="form-control" value={codigoBarras} placeholder="Escanea o escribe el codigo" disabled={guardando || !productos.length}
            onChange={(e) => { setCodigoBarras(e.target.value); setErrorProducto('') }}
            onKeyDown={(e) => { if (e.key === 'Enter') { e.preventDefault(); seleccionarProductoPorCodigo(codigoBarras) } }} />
        </div>
        <div className="col-sm-4 col-md-2">
          <label htmlFor="cantidad" className="form-label">Cantidad</label>
          <input id="cantidad" type="number" min="1" step="1" className="form-control" value={cantidad}
            onChange={(e) => setCantidad(e.target.value)} required disabled={guardando} />
        </div>
        <div className="col-sm-4 col-md-3">
          <label htmlFor="precio" className="form-label">Precio unitario</label>
          <input id="precio" className="form-control" value={moneda.format(ventaEnEdicion?.precioUnitario ?? productoSeleccionado?.precio ?? 0)} readOnly />
        </div>
        {ventaEnEdicion && <div className="col-sm-4 col-md-3">
          <label htmlFor="total-edicion" className="form-label">Nuevo importe</label>
          <input id="total-edicion" className="form-control" value={moneda.format(totalEdicion)} readOnly />
        </div>}
        {!ventaEnEdicion && <div className="col-sm-4 col-md-3 d-grid">
          <button type="button" className="btn btn-outline-dark" onClick={agregarAlCarrito} disabled={guardando || !productos.length}>Agregar al carrito</button>
        </div>}
        {errorProducto && <div className="col-12"><div className="alert alert-warning py-2 mb-0" role="alert">{errorProducto}</div></div>}
      </div>

      {!ventaEnEdicion && <section className="cart-panel" aria-label="Carrito de compra">
        <div className="cart-heading"><div><span className="eyebrow">TICKET ACTUAL</span><h3>Productos de esta compra</h3></div><span className="records-count">{carrito.reduce((n, item) => n + item.cantidad, 0)} piezas</span></div>
        <div className="table-responsive"><table className="table align-middle mb-0">
          <thead><tr><th>Producto</th><th className="text-end">Precio</th><th className="text-end">Cantidad</th><th className="text-end">Importe</th><th><span className="visually-hidden">Accion</span></th></tr></thead>
          <tbody>{carrito.map((item) => <tr key={item.productoId}>
            <td><strong>{item.nombre}</strong><small className="d-block text-muted">{item.codigo}</small></td>
            <td className="text-end">{moneda.format(item.precio)}</td><td className="text-end">{item.cantidad}</td>
            <td className="text-end fw-bold">{moneda.format(item.precio * item.cantidad)}</td>
            <td className="text-end"><button type="button" className="btn btn-sm btn-outline-secondary" onClick={() => quitarDelCarrito(item.productoId)} disabled={guardando}>Quitar</button></td>
          </tr>)}
          {!carrito.length && <tr><td colSpan="5" className="text-center text-muted py-4">El carrito esta vacio. Agrega los productos que lleva el cliente.</td></tr>}</tbody>
        </table></div>
        <div className="cart-total"><span>Total a cobrar</span><strong>{moneda.format(totalCarrito)}</strong></div>
      </section>}

      <div className="d-flex flex-wrap gap-2 mt-3">
        <button type="submit" className="btn btn-neto" disabled={guardando || !sucursales.length || !productos.length || (!ventaEnEdicion && !carrito.length)}>
          {guardando && <span className="spinner-border spinner-border-sm me-2" aria-hidden="true" />}
          {ventaEnEdicion ? 'Guardar cambios' : 'Cobrar y registrar venta'}
        </button>
        {ventaEnEdicion && <button type="button" className="btn btn-outline-secondary" onClick={onCancelar} disabled={guardando}>Cancelar edicion</button>}
        {!ventaEnEdicion && carrito.length > 0 && <button type="button" className="btn btn-outline-secondary" onClick={() => setCarrito([])} disabled={guardando}>Vaciar carrito</button>}
      </div>
    </form>
  )
}

export default FormularioVenta

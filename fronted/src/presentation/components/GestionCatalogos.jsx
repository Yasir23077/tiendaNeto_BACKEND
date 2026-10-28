// Pantalla de administración de productos, precios y sucursales.
import { useState } from 'react'

const moneda = new Intl.NumberFormat('es-MX', { style: 'currency', currency: 'MXN' })

function GestionCatalogos({ productos, sucursales, puedeAdministrar, onCrearProducto, onActualizarProducto, onCrearSucursal }) {
  // Estado de entrada para producto nuevo o para los datos del producto editado.
  const [codigoProducto, setCodigoProducto] = useState('')
  const [nombreProducto, setNombreProducto] = useState('')
  const [categoria, setCategoria] = useState('Abarrotes')
  const [precio, setPrecio] = useState('')
  const [productoEnEdicion, setProductoEnEdicion] = useState(null)
  // Estado del formulario de alta de una sucursal.
  const [codigoSucursal, setCodigoSucursal] = useState('')
  const [nombreSucursal, setNombreSucursal] = useState('')

  // Crea o modifica el producto según el estado de edición y limpia al completar.
  const guardarProducto = async (evento) => {
    evento.preventDefault()
    const datos = { codigo: codigoProducto.trim(), nombre: nombreProducto.trim(), categoria: categoria.trim(), precio: Number(precio) }
    const guardado = productoEnEdicion
      ? await onActualizarProducto(productoEnEdicion.id, datos)
      : await onCrearProducto(datos)
    if (guardado) {
      setCodigoProducto('')
      setNombreProducto('')
      setPrecio('')
      setProductoEnEdicion(null)
    }
  }

  // Copia un producto a los campos del formulario para permitir editarlo.
  const editarProducto = (producto) => {
    setProductoEnEdicion(producto)
    setCodigoProducto(producto.codigo)
    setNombreProducto(producto.nombre)
    setCategoria(producto.categoria || 'General')
    setPrecio(producto.precio)
    document.getElementById('catalogo-productos')?.scrollIntoView({ behavior: 'smooth' })
  }

  // Descarta cambios pendientes y devuelve el formulario a su estado inicial.
  const cancelarEdicion = () => {
    setProductoEnEdicion(null)
    setCodigoProducto('')
    setNombreProducto('')
    setCategoria('Abarrotes')
    setPrecio('')
  }

  // Envía una sucursal nueva y vacía sus campos si se guardó correctamente.
  const guardarSucursal = async (evento) => {
    evento.preventDefault()
    if (await onCrearSucursal({ codigo: codigoSucursal.trim(), nombre: nombreSucursal.trim() })) {
      setCodigoSucursal('')
      setNombreSucursal('')
    }
  }

  return (
    <div className="catalog-grid">
      {/* Catálogo de productos: formulario administrativo y tabla de precios. */}
      <section id="catalogo-productos" className="content-card catalog-card">
        <div className="section-title"><div><p className="eyebrow">CATÁLOGO</p><h2>Productos y precios</h2></div><span className="records-count">{productos.length} productos</span></div>
        <div className="catalog-notice"><b>!</b><span>Confirma y actualiza estos precios con la lista vigente de tu sucursal.</span></div>
        {puedeAdministrar && <form className="catalog-form" onSubmit={guardarProducto}>
          <div><label className="form-label" htmlFor="codigo-producto">Código</label><input id="codigo-producto" className="form-control" value={codigoProducto} onChange={(e) => setCodigoProducto(e.target.value)} placeholder="Ej. A001" required /></div>
          <div><label className="form-label" htmlFor="nombre-producto">Nombre</label><input id="nombre-producto" className="form-control" value={nombreProducto} onChange={(e) => setNombreProducto(e.target.value)} placeholder="Nombre del producto" required /></div>
          <div><label className="form-label" htmlFor="categoria-producto">Categoría</label><input id="categoria-producto" className="form-control" value={categoria} onChange={(e) => setCategoria(e.target.value)} required /></div>
          <div><label className="form-label" htmlFor="precio-producto">Precio MXN</label><input id="precio-producto" type="number" min="0" step="0.01" className="form-control" value={precio} onChange={(e) => setPrecio(e.target.value)} required /></div>
          <div className="catalog-form-actions"><button className="btn btn-neto" type="submit">{productoEnEdicion ? 'Guardar cambios' : 'Agregar producto'}</button>{productoEnEdicion && <button className="btn btn-outline-secondary" type="button" onClick={cancelarEdicion}>Cancelar</button>}</div>
        </form>}
        {!puedeAdministrar && <p className="catalog-readonly">El catálogo está disponible para consulta. Solicita al encargado agregar o actualizar productos.</p>}
        <div className="table-responsive catalog-table-wrap"><table className="table align-middle">
          <thead><tr><th>Código</th><th>Producto</th><th>Categoría</th><th className="text-end">Precio</th>{puedeAdministrar && <th>Acción</th>}</tr></thead>
          <tbody>{productos.map((item) => <tr key={item.id}><td><span className="badge text-bg-light">{item.codigo}</span></td><td className="fw-semibold">{item.nombre}</td><td>{item.categoria || 'General'}</td><td className="text-end fw-bold">{moneda.format(item.precio)}</td>{puedeAdministrar && <td><button className="btn btn-outline-dark btn-sm" type="button" onClick={() => editarProducto(item)}>Editar</button></td>}</tr>)}
            {productos.length === 0 && <tr><td colSpan={puedeAdministrar ? 5 : 4} className="text-center text-muted py-4">Aún no hay productos. Agrega el catálogo de tu sucursal.</td></tr>}</tbody>
        </table></div>
      </section>

      {/* Sucursales habilitadas para asignar operaciones del punto de venta. */}
      <section className="content-card catalog-card branch-card">
        <div className="section-title"><div><p className="eyebrow">OPERACIÓN MULTISUCURSAL</p><h2>Sucursales</h2></div><span className="records-count">{sucursales.length} activas</span></div>
        {puedeAdministrar && <form className="branch-form" onSubmit={guardarSucursal}>
          <div><label className="form-label" htmlFor="codigo-sucursal">Código</label><input id="codigo-sucursal" className="form-control" value={codigoSucursal} onChange={(e) => setCodigoSucursal(e.target.value)} placeholder="SUC-003" required /></div>
          <div><label className="form-label" htmlFor="nombre-sucursal">Nombre de sucursal</label><input id="nombre-sucursal" className="form-control" value={nombreSucursal} onChange={(e) => setNombreSucursal(e.target.value)} placeholder="Nombre o ubicación" required /></div>
          <button className="btn btn-neto" type="submit">Agregar sucursal</button>
        </form>}
        {!puedeAdministrar && <p className="catalog-readonly">El encargado de tienda administra las sucursales disponibles.</p>}
        <div className="branch-list">{sucursales.map((sucursal) => <article className="branch-item" key={sucursal.id}>
          <span className="branch-icon">⌖</span><span><small>{sucursal.codigo}</small><strong>{sucursal.nombre}</strong></span><i>Activa</i>
        </article>)}{!sucursales.length && <p className="catalog-readonly">No hay sucursales registradas.</p>}</div>
      </section>
    </div>
  )
}

export default GestionCatalogos

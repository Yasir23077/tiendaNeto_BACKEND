// Pantalla de administración de productos, precios y sucursales.
import { useState } from 'react'
import Paginacion from './Paginacion'

const moneda = new Intl.NumberFormat('es-MX', { style: 'currency', currency: 'MXN' })

// Función: Presenta los catálogos y permite buscar o modificar datos autorizados.
function GestionCatalogos({ productos, sucursales, puedeAdministrar, cargandoProductos = false, errorProductos = '', onRecargarCatalogos, onBuscarProducto, onCrearProducto, onActualizarProducto, onCrearSucursal }) {
  // Estado de entrada para producto nuevo o para los datos del producto editado.
  const [codigoProducto, setCodigoProducto] = useState('')
  const [nombreProducto, setNombreProducto] = useState('')
  const [categoria, setCategoria] = useState('Abarrotes')
  const [precio, setPrecio] = useState('')
  const [productoEnEdicion, setProductoEnEdicion] = useState(null)
  // Numero de pagina actual de la tabla de productos.
  const [paginaProductos, setPaginaProductos] = useState(1)
  const [busquedaProducto, setBusquedaProducto] = useState('')
  const [busquedaSucursal, setBusquedaSucursal] = useState('')
  const [idBusqueda, setIdBusqueda] = useState('')
  const [cargandoId, setCargandoId] = useState(false)
  const [resultadoId, setResultadoId] = useState(null)
  const [errorId, setErrorId] = useState('')
  const [guardandoProducto, setGuardandoProducto] = useState(false)
  const [erroresProducto, setErroresProducto] = useState({})
  const [errorFormulario, setErrorFormulario] = useState('')
  // Estado del formulario de alta de una sucursal.
  const [codigoSucursal, setCodigoSucursal] = useState('')
  const [nombreSucursal, setNombreSucursal] = useState('')
  // Calcula paginas y filas visibles usando un maximo de diez productos.
  // Busca productos y sucursales en sus identificadores y nombres visibles.
  const productosFiltrados = productos.filter((producto) =>
    [producto.codigo, producto.nombre, producto.categoria].some((valor) =>
      String(valor ?? '').toLocaleLowerCase().includes(busquedaProducto.trim().toLocaleLowerCase())))
  const sucursalesFiltradas = sucursales.filter((sucursal) =>
    [sucursal.codigo, sucursal.nombre].some((valor) =>
      String(valor ?? '').toLocaleLowerCase().includes(busquedaSucursal.trim().toLocaleLowerCase())))
  // Calcula las páginas y extrae únicamente los diez productos de la página actual.
  const totalPaginasProductos = Math.max(1, Math.ceil(productosFiltrados.length / 10))
  const paginaProductosActual = Math.min(paginaProductos, totalPaginasProductos)
  const productosPagina = productosFiltrados.slice((paginaProductosActual - 1) * 10, paginaProductosActual * 10)

  // Crea o modifica el producto según el estado de edición y limpia al completar.
  // Función: Valida el formulario y solicita crear o actualizar el producto.
  const guardarProducto = async (evento) => {
    evento.preventDefault()
    setGuardandoProducto(true)
    setErroresProducto({})
    setErrorFormulario('')
    const datos = { codigo: codigoProducto.trim(), nombre: nombreProducto.trim(), categoria: categoria.trim(), precio: Number(precio) }
    try {
      const guardado = productoEnEdicion
        ? await onActualizarProducto(productoEnEdicion.id, datos)
        : await onCrearProducto(datos)
      if (guardado) {
        setCodigoProducto('')
        setNombreProducto('')
        setCategoria('Abarrotes')
        setPrecio('')
        setProductoEnEdicion(null)
      }
    } catch (error) {
      setErroresProducto(error.errores || {})
      setErrorFormulario(error.message || 'No se pudo guardar el producto.')
    } finally {
      setGuardandoProducto(false)
    }
  }

  const buscarPorId = async (evento) => {
    evento.preventDefault()
    setCargandoId(true)
    setErrorId('')
    setResultadoId(undefined)
    try {
      setResultadoId(await onBuscarProducto(idBusqueda.trim()))
    } catch (error) {
      setErrorId(error.message || 'No se pudo consultar el producto.')
    } finally {
      setCargandoId(false)
    }
  }

  // Copia un producto a los campos del formulario para permitir editarlo.
  // Función: Carga un producto en el formulario de edición.
  const editarProducto = (producto) => {
    setProductoEnEdicion(producto)
    setCodigoProducto(producto.codigo)
    setNombreProducto(producto.nombre)
    setCategoria(producto.categoria || 'General')
    setPrecio(producto.precio)
    document.getElementById('catalogo-productos')?.scrollIntoView({ behavior: 'smooth' })
  }

  // Descarta cambios pendientes y devuelve el formulario a su estado inicial.
  // Función: Limpia el formulario y termina la edición.
  const cancelarEdicion = () => {
    setProductoEnEdicion(null)
    setCodigoProducto('')
    setNombreProducto('')
    setCategoria('Abarrotes')
    setPrecio('')
  }

  // Envía una sucursal nueva y vacía sus campos si se guardó correctamente.
  // Función: Valida la sucursal nueva y la envía al componente principal para guardarla.
  const guardarSucursal = async (evento) => {
    evento.preventDefault()
    if (await onCrearSucursal({ codigo: codigoSucursal.trim(), nombre: nombreSucursal.trim() })) {
      setCodigoSucursal('')
      setNombreSucursal('')
    }
  }

  // Presenta por separado la administración de productos y la lista de sucursales.
  return (
    <div className="catalog-grid">
      {/* Catálogo de productos: formulario administrativo y tabla de precios. */}
      <section id="catalogo-productos" className="content-card catalog-card">
        <div className="section-title"><div><p className="eyebrow">CATÁLOGO</p><h2>Productos y precios</h2></div><div className="d-flex align-items-center gap-2"><span className="records-count">{productos.length} productos</span><button className="btn btn-outline-dark btn-sm" type="button" onClick={onRecargarCatalogos} disabled={cargandoProductos}>{cargandoProductos ? 'Cargando...' : 'Actualizar lista'}</button></div></div>
        <div className="catalog-notice"><b>!</b><span>Confirma y actualiza estos precios con la lista vigente de tu sucursal.</span></div>
        <label className="catalog-search">Buscar producto<input className="form-control" type="search" value={busquedaProducto} onChange={(e) => { setBusquedaProducto(e.target.value); setPaginaProductos(1) }} placeholder="Codigo, nombre o categoria" /></label>
        {puedeAdministrar && <form className="catalog-form" onSubmit={guardarProducto}>
          <div><label className="form-label" htmlFor="codigo-producto">Código</label><input id="codigo-producto" className="form-control" value={codigoProducto} onChange={(e) => setCodigoProducto(e.target.value)} placeholder="Ej. A001" required disabled={!puedeAdministrar || guardandoProducto} aria-invalid={Boolean(erroresProducto.codigo)} />{erroresProducto.codigo && <small className="text-danger" role="alert">{erroresProducto.codigo}</small>}</div>
          <div><label className="form-label" htmlFor="nombre-producto">Nombre</label><input id="nombre-producto" className="form-control" value={nombreProducto} onChange={(e) => setNombreProducto(e.target.value)} placeholder="Nombre del producto" required disabled={!puedeAdministrar || guardandoProducto} aria-invalid={Boolean(erroresProducto.nombre)} />{erroresProducto.nombre && <small className="text-danger" role="alert">{erroresProducto.nombre}</small>}</div>
          <div><label className="form-label" htmlFor="categoria-producto">Categoría</label><input id="categoria-producto" className="form-control" value={categoria} onChange={(e) => setCategoria(e.target.value)} required disabled={!puedeAdministrar || guardandoProducto} aria-invalid={Boolean(erroresProducto.categoria)} />{erroresProducto.categoria && <small className="text-danger" role="alert">{erroresProducto.categoria}</small>}</div>
          <div><label className="form-label" htmlFor="precio-producto">Precio MXN</label><input id="precio-producto" type="number" min="0.01" step="0.01" className="form-control" value={precio} onChange={(e) => setPrecio(e.target.value)} required disabled={!puedeAdministrar || guardandoProducto} aria-invalid={Boolean(erroresProducto.precio)} />{erroresProducto.precio && <small className="text-danger" role="alert">{erroresProducto.precio}</small>}</div>
          {errorFormulario && <p className="text-danger" role="alert">{errorFormulario}</p>}
          <div className="catalog-form-actions"><button className="btn btn-neto" type="submit" disabled={!puedeAdministrar || guardandoProducto}>{guardandoProducto ? 'Guardando...' : productoEnEdicion ? 'Guardar cambios' : 'Agregar producto'}</button>{productoEnEdicion && <button className="btn btn-outline-secondary" type="button" onClick={cancelarEdicion} disabled={guardandoProducto}>Cancelar</button>}</div>
        </form>}
        {!puedeAdministrar && <p className="catalog-readonly">Catálogo de solo lectura. Solicita a un líder o encargado agregar o actualizar productos.</p>}
        <form className="catalog-search" onSubmit={buscarPorId}><label htmlFor="buscar-producto-id">Buscar producto por ID</label><div className="input-group"><input id="buscar-producto-id" className="form-control" type="number" min="1" required value={idBusqueda} onChange={(e) => setIdBusqueda(e.target.value)} placeholder="ID del producto" /><button className="btn btn-outline-dark" disabled={cargandoId}>{cargandoId ? 'Buscando...' : 'Buscar'}</button></div>{errorId && <small className="text-danger" role="alert">{errorId}</small>}{resultadoId === null && <small role="status">Producto no encontrado.</small>}{resultadoId && <p role="status">Encontrado: {resultadoId.nombre} · Código {resultadoId.codigo} · ID {resultadoId.id}</p>}</form>
        <div className="table-responsive catalog-table-wrap"><table className="table align-middle">
          <thead><tr><th>Código</th><th>Producto</th><th>Categoría</th><th className="text-end">Precio</th>{puedeAdministrar && <th>Acción</th>}</tr></thead>
          <tbody>{cargandoProductos ? <tr><td colSpan={puedeAdministrar ? 5 : 4} role="status">Cargando productos...</td></tr> : errorProductos ? <tr><td colSpan={puedeAdministrar ? 5 : 4} className="text-danger" role="alert">{errorProductos}</td></tr> : productosPagina.map((item) => <tr key={item.id}><td><span className="badge text-bg-light">{item.codigo}</span></td><td className="fw-semibold">{item.nombre}</td><td>{item.categoria || 'General'}</td><td className="text-end fw-bold">{moneda.format(item.precio)}</td>{puedeAdministrar && <td><button className="btn btn-outline-dark btn-sm" type="button" onClick={() => editarProducto(item)}>Editar</button></td>}</tr>)}
            {!cargandoProductos && !errorProductos && productosFiltrados.length === 0 && <tr><td colSpan={puedeAdministrar ? 5 : 4} className="text-center text-muted py-4">{productos.length ? 'No hay productos que coincidan con la busqueda.' : 'No hay registros.'}</td></tr>}</tbody>
        </table></div>
        <Paginacion total={productosFiltrados.length} pagina={paginaProductosActual} onCambiar={setPaginaProductos} etiqueta="productos" />
      </section>

      {/* Sucursales habilitadas para asignar operaciones del punto de venta. */}
      <section className="content-card catalog-card branch-card">
        <div className="section-title"><div><p className="eyebrow">OPERACIÓN MULTISUCURSAL</p><h2>Sucursales</h2></div><span className="records-count">{sucursales.length} activas</span></div>
        <label className="catalog-search">Buscar sucursal<input className="form-control" type="search" value={busquedaSucursal} onChange={(e) => setBusquedaSucursal(e.target.value)} placeholder="Codigo o nombre" /></label>
        {puedeAdministrar && <form className="branch-form" onSubmit={guardarSucursal}>
          <div><label className="form-label" htmlFor="codigo-sucursal">Código</label><input id="codigo-sucursal" className="form-control" value={codigoSucursal} onChange={(e) => setCodigoSucursal(e.target.value)} placeholder="SUC-003" required /></div>
          <div><label className="form-label" htmlFor="nombre-sucursal">Nombre de sucursal</label><input id="nombre-sucursal" className="form-control" value={nombreSucursal} onChange={(e) => setNombreSucursal(e.target.value)} placeholder="Nombre o ubicación" required /></div>
          <button className="btn btn-neto" type="submit">Agregar sucursal</button>
        </form>}
        {!puedeAdministrar && <p className="catalog-readonly">Consulta las sucursales. Solo líderes y encargados pueden modificarlas.</p>}
        <div className="branch-list">{sucursalesFiltradas.map((sucursal) => <article className="branch-item" key={sucursal.id}>
          <span className="branch-icon">⌖</span><span><small>{sucursal.codigo}</small><strong>{sucursal.nombre}</strong></span><i>Activa</i>
        </article>)}{!sucursalesFiltradas.length && <p className="catalog-readonly">{sucursales.length ? 'No hay sucursales que coincidan.' : 'No hay sucursales registradas.'}</p>}</div>
      </section>
    </div>
  )
}

export default GestionCatalogos

// Componente raíz: administra sesión, pestañas, ventas, catálogos y auditoría.
import { useEffect, useState } from 'react'
import FormularioVenta from './components/FormularioVenta'
import GestionCatalogos from './components/GestionCatalogos'
import Login from './components/Login'
import NetoLogo from './components/NetoLogo'
import TablaVentas from './components/TablaVentas'
import TablaAuditoria from './components/TablaAuditoria'
import AvisoFlotante from './components/AvisoFlotante'
import { cerrarSesion as cerrarSesionApi, obtenerSesion } from '../services/autenticacionService'
import { listarAuditoria } from '../services/auditoriaService'
import { actualizarProducto, crearProducto, crearSucursal, listarProductos, listarSucursales } from '../services/catalogoService'
import { actualizarVenta, crearVenta, eliminarVenta, listarVentas } from '../services/ventaService'

const moneda = new Intl.NumberFormat('es-MX', { style: 'currency', currency: 'MXN' })

function App() {
  // Recupera la sesión de la pestaña del navegador; JSON inválido abre el login.
  const [usuarioActual, setUsuarioActual] = useState(null)
  const [verificandoSesion, setVerificandoSesion] = useState(true)
  // Datos leídos de la API para las vistas de ventas, resumen y auditoría.
  const [ventas, setVentas] = useState([])
  const [productos, setProductos] = useState([])
  const [sucursales, setSucursales] = useState([])
  // Sucursal elegida para la captura y sucursal seleccionada en el filtro.
  const [sucursalActual, setSucursalActual] = useState('')
  const [filtroSucursal, setFiltroSucursal] = useState('')
  const [busquedaVenta, setBusquedaVenta] = useState('')
  const [fechaInicio, setFechaInicio] = useState('')
  const [fechaFin, setFechaFin] = useState('')
  const [paginaVentas, setPaginaVentas] = useState(1)
  const [cargandoVentas, setCargandoVentas] = useState(false)
  const [cargandoInicial, setCargandoInicial] = useState(false)
  const [errorCargaInicial, setErrorCargaInicial] = useState(false)
  // Estado de navegación y edición del formulario.
  const [pestana, setPestana] = useState('venta')
  const [enEdicion, setEnEdicion] = useState(null)
  // Mensajes de resultado y estado de la consulta de auditoría.
  const [mensaje, setMensaje] = useState(null)
  const [mensajeLogin, setMensajeLogin] = useState('')
  const [auditoria, setAuditoria] = useState([])
  const [cargandoAuditoria, setCargandoAuditoria] = useState(false)
  const [errorAuditoria, setErrorAuditoria] = useState('')

  useEffect(() => {
    let activa = true
    obtenerSesion()
      .then((usuario) => { if (activa) setUsuarioActual(usuario) })
      .catch(() => { if (activa) setUsuarioActual(null) })
      .finally(() => { if (activa) setVerificandoSesion(false) })
    return () => { activa = false }
  }, [])

  useEffect(() => {
    const sesionExpirada = () => {
      setUsuarioActual(null)
      setMensajeLogin('Tu sesión venció. Inicia sesión nuevamente.')
    }
    window.addEventListener('neto:sesion-expirada', sesionExpirada)
    return () => window.removeEventListener('neto:sesion-expirada', sesionExpirada)
  }, [])

  // Descarga del backend el historial de ventas para actualizar las vistas.
  const recargar = async () => {
    setCargandoVentas(true)
    try {
      setVentas(await listarVentas())
      return true
    } catch (error) {
      setMensaje({ texto: `No se pudieron cargar las ventas: ${error.message}`, error: true })
      return false
    } finally {
      setCargandoVentas(false)
    }
  }

  // Carga productos y sucursales en paralelo y conserva una sucursal válida.
  const cargarCatalogos = async () => {
    try {
      const [listaProductos, listaSucursales] = await Promise.all([listarProductos(), listarSucursales()])
      setProductos(listaProductos)
      setSucursales(listaSucursales)
      setSucursalActual((actual) => listaSucursales.some((item) => item.nombre === actual) ? actual : (listaSucursales[0]?.nombre ?? ''))
      return true
    } catch (error) {
      setMensaje({ texto: `No se pudo cargar el catálogo: ${error.message}`, error: true })
      return false
    }
  }

  // Al iniciar sesión carga los datos necesarios para operar la aplicación.
  useEffect(() => {
    if (usuarioActual) {
      const cargarInicial = async () => {
        setCargandoInicial(true)
        setErrorCargaInicial(false)
        try {
          const resultados = await Promise.all([recargar(), cargarCatalogos()])
          setErrorCargaInicial(resultados.some((resultado) => !resultado))
        } finally {
          setCargandoInicial(false)
        }
      }
      cargarInicial()
    }
  }, [usuarioActual])

  // Consulta la bitácora solo al entrar a esa pestaña con el perfil encargado.
  useEffect(() => {
    if (pestana !== 'auditoria' || usuarioActual?.role !== 'encargado') return
    let activa = true
    setCargandoAuditoria(true)
    setErrorAuditoria('')
    listarAuditoria().then((datos) => { if (activa) setAuditoria(datos) })
      .catch((error) => { if (activa) setErrorAuditoria(error.message) })
      .finally(() => { if (activa) setCargandoAuditoria(false) })
    return () => { activa = false }
  }, [pestana, usuarioActual])

  const iniciarSesion = (usuario) => {
    setMensajeLogin('')
    setUsuarioActual(usuario)
  }

  // Limpia la sesión y el estado temporal al salir del sistema.
  const cerrarSesion = async () => {
    try {
      await cerrarSesionApi()
    } catch (error) {
      setMensaje({ texto: `La sesión local se cerró; el servidor respondió: ${error.message}`, error: true })
    } finally {
      setUsuarioActual(null)
      setEnEdicion(null)
      setPestana('venta')
      setAuditoria([])
    }
  }

  const puedeEditar = usuarioActual?.role === 'lider' || usuarioActual?.role === 'encargado'
  // Solo el encargado puede borrar, administrar catálogos y ver auditoría.
  const puedeEliminar = usuarioActual?.role === 'encargado'
  const puedeAdministrar = usuarioActual?.role === 'encargado'

  if (verificandoSesion) return <div className="loading-overlay"><div className="loading-dialog"><span className="spinner-border text-primary" /><strong>Verificando sesión</strong></div></div>
  if (!usuarioActual) return <Login onLogin={iniciarSesion} mensajeInicial={mensajeLogin} />

  // Decide entre crear y actualizar una venta, y refresca el historial.
  const guardar = async (datos) => {
    try {
      if (enEdicion) {
        await actualizarVenta(enEdicion.id, datos)
        setMensaje({ texto: 'Venta actualizada correctamente.' })
      } else {
        await crearVenta(datos)
        setMensaje({ texto: 'Venta registrada correctamente.' })
      }
      setEnEdicion(null)
      await recargar()
      return true
    } catch (error) {
      setMensaje({ texto: `No se pudo guardar la venta: ${error.message}`, error: true })
      return false
    }
  }

  // Confirma con el usuario antes de eliminar una venta.
  const eliminar = async (id) => {
    if (!window.confirm('¿Seguro que deseas eliminar este registro de venta?')) return
    try {
      await eliminarVenta(id)
      setMensaje({ texto: 'Registro de venta eliminado.' })
      if (enEdicion?.id === id) setEnEdicion(null)
      await recargar()
    } catch (error) {
      setMensaje({ texto: `No se pudo eliminar: ${error.message}`, error: true })
    }
  }

  // Crea un producto de catálogo y vuelve a cargar los catálogos.
  const guardarProducto = async (datos) => {
    try {
      await crearProducto(datos)
      setMensaje({ texto: 'Producto agregado al catálogo.' })
      await cargarCatalogos()
      return true
    } catch (error) {
      setMensaje({ texto: `No se pudo agregar el producto: ${error.message}`, error: true })
      return false
    }
  }

  // Actualiza los datos de un producto existente.
  const actualizarProductoCatalogo = async (id, datos) => {
    try {
      await actualizarProducto(id, datos)
      setMensaje({ texto: 'Producto y precio actualizados.' })
      await cargarCatalogos()
      return true
    } catch (error) {
      setMensaje({ texto: `No se pudo actualizar el producto: ${error.message}`, error: true })
      return false
    }
  }

  // Crea una sucursal y actualiza las opciones del formulario de venta.
  const guardarSucursal = async (datos) => {
    try {
      await crearSucursal(datos)
      setMensaje({ texto: 'Sucursal agregada correctamente.' })
      await cargarCatalogos()
      return true
    } catch (error) {
      setMensaje({ texto: `No se pudo agregar la sucursal: ${error.message}`, error: true })
      return false
    }
  }

  // Métricas derivadas del historial para mostrar el resumen general y por sucursal.
  const totalVentas = ventas.reduce((suma, venta) => suma + Number(venta.total || 0), 0)
  const promedioVenta = ventas.length ? totalVentas / ventas.length : 0
  const ventasVisibles = ventas.filter((venta) => !filtroSucursal || (venta.sucursal || 'Sin sucursal') === filtroSucursal)
  const ventasFiltradas = ventasVisibles.filter((venta) => {
    const texto = busquedaVenta.trim().toLocaleLowerCase()
    const coincideTexto = !texto || [venta.id, venta.producto, venta.sucursal, venta.cantidad]
      .some((valor) => String(valor ?? '').toLocaleLowerCase().includes(texto))
    const fecha = venta.fecha?.slice(0, 10) ?? ''
    return coincideTexto && (!fechaInicio || fecha >= fechaInicio) && (!fechaFin || fecha <= fechaFin)
  })
  const ventasPorPagina = 10
  const totalPaginasVentas = Math.max(1, Math.ceil(ventasFiltradas.length / ventasPorPagina))
  const paginaActual = Math.min(paginaVentas, totalPaginasVentas)
  const ventasPagina = ventasFiltradas.slice((paginaActual - 1) * ventasPorPagina, paginaActual * ventasPorPagina)
  const titulosVista = {
    venta: ['Registro de ventas', 'Captura una operación en el punto de venta.'],
    historial: ['Historial de ventas', 'Busca y consulta las operaciones registradas.'],
    resumen: ['Resumen de ventas', 'Consulta el desempeño de las sucursales.'],
    catalogos: ['Productos y sucursales', 'Administra los datos de operación de la tienda.'],
    auditoria: ['Auditoría', 'Consulta los cambios realizados en el sistema.'],
  }
  const resumenSucursales = ventas.reduce((resumen, venta) => {
    const nombre = venta.sucursal || 'Sin sucursal'
    const actual = resumen.get(nombre) || { nombre, cantidad: 0, total: 0 }
    actual.cantidad += 1
    actual.total += Number(venta.total || 0)
    resumen.set(nombre, actual)
    return resumen
  }, new Map())

  return (
    <div className="erp-app">
      <AvisoFlotante mensaje={mensaje} onCerrar={setMensaje} />
      {/* Barra de identidad, sucursal y sesión activa. */}
      <header className="erp-header">
        <div className="erp-brand"><NetoLogo /><span className="erp-brand-word"><small>SISTEMA DE VENTAS</small></span></div>
        <div className="erp-store"><strong>Sistema de operación</strong><span>Sucursal · Punto de venta</span></div>
        <div className="erp-account"><span className="account-name">{usuarioActual.name}<small>{usuarioActual.roleLabel}</small></span><div className="user-avatar">{usuarioActual.initials?.slice(0, 1) ?? 'U'}</div><button className="logout-button" onClick={cerrarSesion}>Cerrar sesión</button></div>
      </header>
      {/* Navegación entre los módulos habilitados para el perfil actual. */}
      <nav className="erp-nav" aria-label="Navegación principal">
        <span className="erp-nav-label">☰ <span>MENÚ PRINCIPAL</span></span>
        <button className={`erp-nav-link ${pestana === 'venta' ? 'active' : ''}`} onClick={() => setPestana('venta')}>▣ <span>Punto de venta</span></button>
        <button className={`erp-nav-link ${pestana === 'historial' ? 'active' : ''}`} onClick={() => setPestana('historial')}>▤ <span>Historial</span></button>
        <button className={`erp-nav-link ${pestana === 'resumen' ? 'active' : ''}`} onClick={() => setPestana('resumen')}>▥ <span>Resumen</span></button>
        <button className={`erp-nav-link ${pestana === 'catalogos' ? 'active' : ''}`} onClick={() => setPestana('catalogos')}>▦ <span>Productos y sucursales</span></button>
        {puedeAdministrar && <button className={`erp-nav-link ${pestana === 'auditoria' ? 'active' : ''}`} onClick={() => setPestana('auditoria')}>◷ <span>Auditoría</span></button>}
        <span className="erp-nav-status"><i /> SISTEMA ACTIVO</span>
      </nav>

      <main className="neto-shell erp-page">
        {/* Contexto de la pantalla y mensajes de resultado de las operaciones. */}
        <div className="erp-breadcrumb"><span>OPERACIÓN DE SUCURSAL</span><b>/</b><strong>PUNTO DE VENTA</strong></div>
        <section className="erp-page-heading"><div><h1>{titulosVista[pestana]?.[0] ?? 'Sistema de ventas'}</h1><p>{titulosVista[pestana]?.[1]}</p></div><span className="date-chip">PANEL NETO</span></section>
        {cargandoInicial && <div className="loading-overlay" role="status" aria-live="polite"><div className="loading-dialog"><span className="spinner-border text-primary" aria-hidden="true" /><strong>Conectando con el sistema</strong><span>Cargando ventas, productos y sucursales…</span></div></div>}
        {errorCargaInicial && <div className="load-error" role="alert"><span>No se pudieron cargar todos los datos. Revisa la conexión con el servidor.</span><button className="btn btn-neto btn-sm" type="button" onClick={async () => {
          setCargandoInicial(true)
          setErrorCargaInicial(false)
          const resultados = await Promise.all([recargar(), cargarCatalogos()])
          setErrorCargaInicial(resultados.some((resultado) => !resultado))
          setCargandoInicial(false)
        }}>Reintentar</button></div>}

        {/* Vista de captura de ventas y datos de operación. */}
        {pestana === 'venta' && <div className="erp-workspace">
          <aside className="erp-sidebar">
            <div className="sidebar-heading"><span>NETO</span><strong>EXPRESS</strong></div>
            <p className="sidebar-label">MÓDULO DE VENTAS</p>
            <button className="side-link active" onClick={() => document.getElementById('registro')?.scrollIntoView({ behavior: 'smooth' })}><span className="side-icon">＋</span><span>Nueva venta</span><b>›</b></button>
            <button className="side-link" onClick={() => setPestana('historial')}><span className="side-icon">▤</span><span>Ventas registradas</span><b className="side-count">{ventas.length}</b></button>
            <div className="sidebar-catalog-hint"><strong>{productos.length} productos</strong><span>en catálogo</span><strong>{sucursales.length} sucursales</strong><span>disponibles</span></div>
            <div className="sidebar-profile"><span className="profile-mini">{usuarioActual.initials?.slice(0, 1) ?? 'U'}</span><div><small>SESIÓN ACTIVA</small><strong>{usuarioActual.roleLabel}</strong></div></div>
          </aside>

          <div className="erp-main-content">
            <section id="registro" className="content-card capture-card">
              <div className="section-title"><div className="title-with-mark"><span className="title-mark">+</span><div><p className="eyebrow">PUNTO DE VENTA</p><h2>{enEdicion ? 'Editar venta' : 'Registrar una venta'}</h2></div></div>
                <span className="live-indicator"><i /> Listo para operar</span></div>
              <FormularioVenta key={enEdicion?.id ?? 'nueva'} ventaEnEdicion={enEdicion} productos={productos} sucursales={sucursales} sucursalActual={sucursalActual} onSucursalChange={setSucursalActual} onGuardar={guardar} onCancelar={() => setEnEdicion(null)} />
            </section>
            <div className="quick-tip"><span>i</span><p>Escribe el nombre de un producto y selecciónalo de las sugerencias para completar el precio automáticamente.</p></div>
          </div>
        </div>}

        {/* Historial de ventas y filtro por sucursal. */}
        {pestana === 'historial' && <section className="content-card history-card full-panel">
          <div className="section-title"><div><p className="eyebrow">MOVIMIENTOS DE LA SUCURSAL</p><h2>Historial de ventas</h2></div><span className="records-count">{ventasFiltradas.length} resultados</span></div>
          <div className="history-filters">
            <label>Buscar<input className="form-control" type="search" value={busquedaVenta} onChange={(e) => { setBusquedaVenta(e.target.value); setPaginaVentas(1) }} placeholder="Folio, producto, cantidad o sucursal" /></label>
            <label>Sucursal<select className="form-select" value={filtroSucursal} onChange={(e) => { setFiltroSucursal(e.target.value); setPaginaVentas(1) }}><option value="">Todas las sucursales</option><option value="Sin sucursal">Ventas antiguas sin sucursal</option>{sucursales.map((item) => <option key={item.id} value={item.nombre}>{item.nombre}</option>)}</select></label>
            <label>Desde<input className="form-control" type="date" value={fechaInicio} onChange={(e) => { setFechaInicio(e.target.value); setPaginaVentas(1) }} /></label>
            <label>Hasta<input className="form-control" type="date" value={fechaFin} onChange={(e) => { setFechaFin(e.target.value); setPaginaVentas(1) }} /></label>
            <button className="btn btn-outline-secondary clear-filters" type="button" onClick={() => { setBusquedaVenta(''); setFiltroSucursal(''); setFechaInicio(''); setFechaFin(''); setPaginaVentas(1) }}>Limpiar filtros</button>
          </div>
          <TablaVentas ventas={ventasPagina} cargando={cargandoVentas} onEditar={(venta) => { setEnEdicion(venta); setPestana('venta') }} onEliminar={eliminar} puedeEditar={puedeEditar} puedeEliminar={puedeEliminar} />
          <div className="history-pagination"><span>Mostrando {ventasFiltradas.length ? (paginaActual - 1) * ventasPorPagina + 1 : 0}–{Math.min(paginaActual * ventasPorPagina, ventasFiltradas.length)} de {ventasFiltradas.length}</span><div><button className="btn btn-outline-secondary btn-sm" type="button" disabled={paginaActual <= 1} onClick={() => setPaginaVentas(paginaActual - 1)}>Anterior</button><span>Página {paginaActual} de {totalPaginasVentas}</span><button className="btn btn-outline-secondary btn-sm" type="button" disabled={paginaActual >= totalPaginasVentas} onClick={() => setPaginaVentas(paginaActual + 1)}>Siguiente</button></div></div>
        </section>}

        {/* Métricas agregadas y comparativo de sucursales. */}
        {pestana === 'resumen' && <section className="summary-page">
          <div className="metrics-grid" aria-label="Resumen de ventas">
            <article className="metric-card"><span className="metric-icon orange-icon">$</span><div><small>VENTA ACUMULADA</small><strong>{moneda.format(totalVentas)}</strong><span className="metric-note">Suma de operaciones</span></div></article>
            <article className="metric-card"><span className="metric-icon blue-icon">▤</span><div><small>TRANSACCIONES</small><strong>{ventas.length}</strong><span className="metric-note">Ventas registradas</span></div></article>
            <article className="metric-card"><span className="metric-icon pale-icon">◷</span><div><small>PROMEDIO POR VENTA</small><strong>{moneda.format(promedioVenta)}</strong><span className="metric-note">Por registro</span></div></article>
          </div>
          <section className="content-card history-card branch-summary-card"><div className="section-title"><div><p className="eyebrow">COMPARATIVO</p><h2>Actividad por sucursal</h2></div><span className="records-count">{sucursales.length} sucursales</span></div>
            <div className="table-responsive"><table className="table align-middle"><thead><tr><th>Sucursal</th><th className="text-end">Transacciones</th><th className="text-end">Venta acumulada</th></tr></thead><tbody>
              {sucursales.map((item) => { const resumen = resumenSucursales.get(item.nombre) || { cantidad: 0, total: 0 }; return <tr key={item.id}><td className="fw-semibold">{item.nombre}</td><td className="text-end">{resumen.cantidad}</td><td className="text-end fw-bold">{moneda.format(resumen.total)}</td></tr> })}
              {resumenSucursales.has('Sin sucursal') && <tr><td>Ventas antiguas sin sucursal</td><td className="text-end">{resumenSucursales.get('Sin sucursal').cantidad}</td><td className="text-end">{moneda.format(resumenSucursales.get('Sin sucursal').total)}</td></tr>}
            </tbody></table></div>
          </section>
        </section>}

        {/* Mantenimiento de los catálogos de productos y sucursales. */}
        {pestana === 'catalogos' && <GestionCatalogos productos={productos} sucursales={sucursales} puedeAdministrar={puedeAdministrar} onCrearProducto={guardarProducto} onActualizarProducto={actualizarProductoCatalogo} onCrearSucursal={guardarSucursal} />}
        {/* Bitácora restringida visualmente al perfil encargado. */}
        {pestana === 'auditoria' && puedeAdministrar && <TablaAuditoria registros={auditoria} cargando={cargandoAuditoria} error={errorAuditoria} />}
        <footer className="page-footer"><span>NETO VENTAS</span><span>Administración de sucursal · MXN</span></footer>
      </main>
    </div>
  )
}

export default App

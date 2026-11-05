// Componente raíz: administra sesión, pestañas, ventas, catálogos y auditoría.
import { useEffect, useRef, useState } from 'react'
import FormularioVenta from './componentes/FormularioVenta'
import GestionCatalogos from './componentes/GestionCatalogos'
import InicioSesion from './componentes/InicioSesion'
import LogoNeto from './componentes/LogoNeto'
import TablaVentas from './componentes/TablaVentas'
import TablaAuditoria from './componentes/TablaAuditoria'
import AdministracionUsuarios from './componentes/AdministracionUsuarios'
import Paginacion from './componentes/Paginacion'
import AvisoFlotante from './componentes/AvisoFlotante'
import { cerrarSesion as cerrarSesionApi, obtenerSesion } from '../servicios/servicioAutenticacion'
import { listarAuditoria } from '../servicios/servicioAuditoria'
import { actualizarUsuario as actualizarUsuarioApi, crearUsuario, listarUsuarios } from '../servicios/servicioAdministracionUsuarios'
import { actualizarProducto, crearProducto, crearSucursal, listarProductos, listarSucursales, obtenerProducto } from '../servicios/servicioCatalogo'
import { actualizarVenta, crearVentaLote, eliminarVenta, listarVentas } from '../servicios/servicioVenta'

const moneda = new Intl.NumberFormat('es-MX', { style: 'currency', currency: 'MXN' })

// Función: Componente raíz que conserva sesión y datos, organiza pestañas y conecta acciones con servicios.
function Aplicacion() {
  // Permite cerrar el menú desplegable desde sus opciones de navegación.
  const menuPrincipalRef = useRef(null)
  // Recupera la sesión de la pestaña del navegador; JSON inválido abre el login.
  const [usuarioActual, setUsuarioActual] = useState(null)
  const [verificandoSesion, setVerificandoSesion] = useState(true)
  // Datos leídos de la API para las vistas de ventas, resumen y auditoría.
  const [ventas, setVentas] = useState([])
  const [productos, setProductos] = useState([])
  // Controla la carga del catálogo y el error que debe mostrarse en la tabla.
  const [cargandoProductos, setCargandoProductos] = useState(false)
  const [errorProductos, setErrorProductos] = useState('')
  const [sucursales, setSucursales] = useState([])
  // Sucursal elegida para la captura y sucursal seleccionada en el filtro.
  const [sucursalActual, setSucursalActual] = useState('')
  const [filtroSucursal, setFiltroSucursal] = useState('')
  const [busquedaVenta, setBusquedaVenta] = useState('')
  const [fechaInicio, setFechaInicio] = useState('')
  const [fechaFin, setFechaFin] = useState('')
  const [paginaVentas, setPaginaVentas] = useState(1)
  // Mantiene aparte la pagina de sucursales mostrada en el resumen.
  const [paginaResumen, setPaginaResumen] = useState(1)
  const [busquedaResumen, setBusquedaResumen] = useState('')
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
  // Cuentas y estado de carga de la administracion de usuarios.
  const [usuarios, setUsuarios] = useState([])
  const [cargandoUsuarios, setCargandoUsuarios] = useState(false)
  const [errorUsuarios, setErrorUsuarios] = useState('')

  useEffect(() => {
    // Evita cambiar estado si la respuesta llega despues de desmontar el componente.
    let activa = true
    // Pregunta al backend si la cookie del navegador conserva una sesion valida.
    obtenerSesion()
      .then((usuario) => { if (activa) setUsuarioActual(usuario) })
      .catch(() => { if (activa) setUsuarioActual(null) })
      .finally(() => { if (activa) setVerificandoSesion(false) })
    return () => { activa = false }
  }, [])

  useEffect(() => {
    // Atiende el evento que el cliente HTTP emite al recibir un 401.
    // Función: Vuelve al acceso cuando vence la sesión.
    const sesionExpirada = () => {
      setUsuarioActual(null)
      setMensajeLogin('Tu sesión venció. Inicia sesión nuevamente.')
    }
    // Muestra otra vez el login cuando el servidor informa que la sesion expiro.
    window.addEventListener('neto:sesion-expirada', sesionExpirada)
    return () => window.removeEventListener('neto:sesion-expirada', sesionExpirada)
  }, [])

  // Descarga del backend el historial de ventas para actualizar las vistas.
  // Función: Pide el historial, actualiza la pantalla y comunica errores.
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
  // Función: Carga productos y sucursales en paralelo.
  const cargarCatalogos = async () => {
    setCargandoProductos(true)
    setErrorProductos('')
    try {
      const [listaProductos, listaSucursales] = await Promise.all([listarProductos(), listarSucursales()])
      setProductos(listaProductos)
      setSucursales(listaSucursales)
      setSucursalActual((actual) => listaSucursales.some((item) => item.nombre === actual) ? actual : (listaSucursales[0]?.nombre ?? ''))
      return true
    } catch (error) {
      setErrorProductos(`No se pudo cargar el catálogo: ${error.message}`)
      setMensaje({ texto: `No se pudo cargar el catálogo: ${error.message}`, error: true })
      return false
    } finally {
      setCargandoProductos(false)
    }
  }

  // Al iniciar sesión carga los datos necesarios para operar la aplicación.
  useEffect(() => {
    if (usuarioActual) {
      // Función: Carga datos de trabajo cuando existe una sesión.
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

  // Consulta las cuentas solo cuando el encargado abre la pestana de usuarios.
  useEffect(() => {
    if (pestana !== 'usuarios' || usuarioActual?.role !== 'encargado') return
    let activa = true
    setCargandoUsuarios(true)
    setErrorUsuarios('')
    listarUsuarios()
      .then((lista) => { if (activa) setUsuarios(lista) })
      .catch((error) => { if (activa) setErrorUsuarios(error.message) })
      .finally(() => { if (activa) setCargandoUsuarios(false) })
    return () => { activa = false }
  }, [pestana, usuarioActual])

  // Función: Guarda el perfil de usuario confirmado por el servidor.
  const iniciarSesion = (usuario) => {
    // Conserva el perfil que devolvio el backend y borra el mensaje de login.
    setMensajeLogin('')
    setUsuarioActual(usuario)
  }

  // Limpia la sesión y el estado temporal al salir del sistema.
  // Función: Solicita cerrar sesión y limpia el estado de la interfaz.
  const cerrarSesion = async () => {
    try {
      // Solicita al backend invalidar la sesion actual.
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

  // Cambia de sección y cierra el menú para dejar visible el contenido elegido.
  const navegarDesdeMenu = (destino) => {
    setPestana(destino)
    if (menuPrincipalRef.current) menuPrincipalRef.current.open = false
  }

  // Cierra el menú y ejecuta el mismo flujo seguro de cierre de sesión del encabezado.
  const salirDesdeMenu = () => {
    if (menuPrincipalRef.current) menuPrincipalRef.current.open = false
    cerrarSesion()
  }

  // Cualquier perfil autenticado puede registrar nuevas ventas.
  const puedeRegistrarVenta = Boolean(usuarioActual)
  const puedeModificar = ['lider', 'encargado'].includes(usuarioActual?.role)
  const puedeEditar = puedeModificar
  const puedeEliminar = puedeModificar
  const puedeGestionarCatalogos = puedeModificar
  // El encargado conserva el acceso exclusivo a auditoria y gestion de usuarios.
  const puedeAdministrar = usuarioActual?.role === 'encargado'

  if (verificandoSesion) return <div className="loading-overlay"><div className="loading-dialog"><span className="spinner-border text-primary" /><strong>Verificando sesión</strong></div></div>
  if (!usuarioActual) return <InicioSesion onLogin={iniciarSesion} mensajeInicial={mensajeLogin} />

  // Decide entre crear y actualizar una venta, y refresca el historial.
  // Función: Valida y envía el formulario para crear o modificar el registro.
  const guardar = async (datos) => {
    try {
      if (enEdicion) {
        await actualizarVenta(enEdicion.id, datos)
        setMensaje({ texto: 'Venta actualizada correctamente.' })
      } else {
        const lineas = await crearVentaLote(datos)
        const total = lineas.reduce((suma, item) => suma + Number(item.total), 0)
        const piezas = lineas.reduce((suma, item) => suma + Number(item.cantidad), 0)
        setMensaje({ texto: `Venta ${lineas[0]?.folio ?? ''} registrada: ${piezas} piezas por ${moneda.format(total)}.` })
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
  // Función: Solicita borrar una venta y actualiza la lista.
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
  // Función: Elige el servicio de alta o actualización, guarda el producto y vuelve a cargar el catálogo.
  const guardarProducto = async (datos) => {
    try {
      await crearProducto(datos)
      setMensaje({ texto: 'Producto agregado al catálogo.' })
      await cargarCatalogos()
      return true
    } catch (error) {
      setMensaje({ texto: `No se pudo agregar el producto: ${error.message}`, error: true })
      throw error
    }
  }

  // Actualiza los datos de un producto existente.
  // Función: Envía cambios del producto y refresca el catálogo.
  const actualizarProductoCatalogo = async (id, datos) => {
    try {
      await actualizarProducto(id, datos)
      setMensaje({ texto: 'Producto y precio actualizados.' })
      await cargarCatalogos()
      return true
    } catch (error) {
      setMensaje({ texto: `No se pudo actualizar el producto: ${error.message}`, error: true })
      throw error
    }
  }

  // Crea una sucursal y actualiza las opciones del formulario de venta.
  // Función: Envía la sucursal nueva al backend, recarga los catálogos y comunica el resultado.
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
  // Crea una cuenta mediante la API protegida y actualiza la lista de usuarios.
  // Función: Solicita crear la cuenta, actualiza la lista de usuarios y comunica si hubo un error.
  const guardarUsuario = async (datos) => {
    try {
      await crearUsuario(datos)
      setMensaje({ texto: 'Usuario creado correctamente.' })
      try {
        setUsuarios(await listarUsuarios())
        setErrorUsuarios('')
      } catch (error) {
        setErrorUsuarios(`El usuario se creo, pero no se pudo actualizar la lista: ${error.message}`)
      }
      return true
    } catch (error) {
      setMensaje({ texto: `No se pudo crear el usuario: ${error.message}`, error: true })
      return false
    }
  }

  // Actualiza una cuenta y vuelve a consultar la lista administrativa.
  // Función: Envía al servidor los cambios de una cuenta.
  const actualizarUsuarioAdmin = async (id, datos) => {
    try {
      await actualizarUsuarioApi(id, datos)
      setMensaje({ texto: 'Usuario actualizado correctamente.' })
      try {
        setUsuarios(await listarUsuarios())
        setErrorUsuarios('')
      } catch (error) {
        setErrorUsuarios(`El usuario se actualizo, pero no se pudo actualizar la lista: ${error.message}`)
      }
      return true
    } catch (error) {
      setMensaje({ texto: `No se pudo actualizar el usuario: ${error.message}`, error: true })
      return false
    }
  }

  // Suma el importe de cada renglón recibido para obtener la venta acumulada.
  const totalVentas = ventas.reduce((suma, venta) => suma + Number(venta.total || 0), 0)
  // Cuenta folios únicos para no tratar cada producto de un mismo ticket como venta aparte.
  const totalTransacciones = new Set(ventas.map((venta) => venta.folio || `REG-${venta.id}`)).size
  // Evita dividir entre cero cuando todavía no existen ventas.
  const promedioVenta = totalTransacciones ? totalVentas / totalTransacciones : 0
  // Aplica primero el filtro por sucursal para reducir los registros del historial.
  const ventasVisibles = ventas.filter((venta) => !filtroSucursal || (venta.sucursal || 'Sin sucursal') === filtroSucursal)
  // Filtra por texto y por el rango de fechas seleccionado.
  const ventasFiltradas = ventasVisibles.filter((venta) => {
    const texto = busquedaVenta.trim().toLocaleLowerCase()
    const coincideTexto = !texto || [venta.id, venta.folio, venta.producto, venta.sucursal, venta.cantidad]
      .some((valor) => String(valor ?? '').toLocaleLowerCase().includes(texto))
    const fecha = venta.fecha?.slice(0, 10) ?? ''
    return coincideTexto && (!fechaInicio || fecha >= fechaInicio) && (!fechaFin || fecha <= fechaFin)
  })
  // Limita las filas del historial para mantener la tabla compacta.
  const ventasPorPagina = 10
  const totalPaginasVentas = Math.max(1, Math.ceil(ventasFiltradas.length / ventasPorPagina))
  const paginaActual = Math.min(paginaVentas, totalPaginasVentas)
  const ventasPagina = ventasFiltradas.slice((paginaActual - 1) * ventasPorPagina, paginaActual * ventasPorPagina)
  // Asocia a cada pestaña su título y descripción para el encabezado principal.
  const titulosVista = {
    usuarios: ['Administracion de usuarios', 'Crea cuentas y asigna los permisos de cada perfil.'],
    venta: ['Registro de ventas', 'Captura una operación en el punto de venta.'],
    historial: ['Historial de ventas', 'Busca y consulta las operaciones registradas.'],
    resumen: ['Resumen de ventas', 'Consulta el desempeño de las sucursales.'],
    catalogos: ['Productos y sucursales', 'Administra los datos de operación de la tienda.'],
    auditoria: ['Auditoría', 'Consulta los cambios realizados en el sistema.'],
  }
  // Agrupa ventas por sucursal y cuenta folios distintos dentro de cada grupo.
  const resumenSucursales = ventas.reduce((resumen, venta) => {
    const nombre = venta.sucursal || 'Sin sucursal'
    const actual = resumen.get(nombre) || { nombre, folios: new Set(), total: 0 }
    actual.folios.add(venta.folio || `REG-${venta.id}`)
    actual.total += Number(venta.total || 0)
    resumen.set(nombre, actual)
    return resumen
  }, new Map())
  // Prepara una fila por sucursal y agrega las ventas antiguas sin sucursal.
  const filasResumenSucursales = sucursales.map((item) => {
    const resumen = resumenSucursales.get(item.nombre) || { cantidad: 0, total: 0 }
    return { id: item.id, nombre: item.nombre, cantidad: resumen.folios?.size ?? resumen.cantidad, total: resumen.total }
  })
  if (resumenSucursales.has('Sin sucursal')) {
    const resumen = resumenSucursales.get('Sin sucursal')
    filasResumenSucursales.push({ id: 'sin-sucursal', nombre: 'Ventas antiguas sin sucursal', cantidad: resumen.folios?.size ?? resumen.cantidad, total: resumen.total })
  }
  // Filtra sucursales del resumen usando el texto escrito en el buscador.
  const sucursalesResumenFiltradas = filasResumenSucursales.filter((item) =>
    item.nombre.toLocaleLowerCase().includes(busquedaResumen.trim().toLocaleLowerCase()))
  // Muestra como maximo diez sucursales por pagina en la tabla del resumen.
  // Calcula la página disponible y recorta la lista a diez sucursales.
  const totalPaginasResumen = Math.max(1, Math.ceil(sucursalesResumenFiltradas.length / 10))
  const paginaResumenActual = Math.min(paginaResumen, totalPaginasResumen)
  const filasResumenPagina = sucursalesResumenFiltradas.slice((paginaResumenActual - 1) * 10, paginaResumenActual * 10)

  return (
    <div className="erp-app">
      <AvisoFlotante mensaje={mensaje} onCerrar={setMensaje} />
      {/* Barra de identidad, sucursal y sesión activa. */}
      <header className="erp-header">
        <div className="erp-brand"><LogoNeto /><span className="erp-brand-word"><small>SISTEMA DE VENTAS</small></span></div>
        <div className="erp-store"><strong>Sistema de operación</strong><span>Sucursal · Punto de venta</span></div>
        <div className="erp-account"><span className="account-name">{usuarioActual.name}<small>{usuarioActual.roleLabel}</small></span><div className="user-avatar">{usuarioActual.initials?.slice(0, 1) ?? 'U'}</div></div>
      </header>
      {/* Menú desplegable que agrupa los módulos por tema y ofrece cerrar sesión. */}
      <nav className="erp-nav" aria-label="Navegación principal">
        <details className="erp-menu-dropdown" ref={menuPrincipalRef}>
          <summary className="erp-menu-trigger"><span aria-hidden="true">☰</span> Menú principal <span className="erp-menu-chevron" aria-hidden="true">⌄</span></summary>
          <div className="erp-menu-panel">
            <div className="erp-menu-group">
              <span className="erp-menu-group-title">VENTAS</span>
              {puedeRegistrarVenta && <button type="button" className={`erp-menu-item ${pestana === 'venta' ? 'active' : ''}`} onClick={() => navegarDesdeMenu('venta')}><span aria-hidden="true">▣</span> Punto de venta</button>}
              <button type="button" className={`erp-menu-item ${pestana === 'historial' ? 'active' : ''}`} onClick={() => navegarDesdeMenu('historial')}><span aria-hidden="true">▤</span> Historial</button>
              <button type="button" className={`erp-menu-item ${pestana === 'resumen' ? 'active' : ''}`} onClick={() => navegarDesdeMenu('resumen')}><span aria-hidden="true">▥</span> Resumen</button>
            </div>
            <div className="erp-menu-group">
              <span className="erp-menu-group-title">CATÁLOGOS</span>
              <button type="button" className={`erp-menu-item ${pestana === 'catalogos' ? 'active' : ''}`} onClick={() => navegarDesdeMenu('catalogos')}><span aria-hidden="true">▦</span> Productos y sucursales</button>
            </div>
            {puedeAdministrar && <div className="erp-menu-group">
              <span className="erp-menu-group-title">ADMINISTRACIÓN</span>
              <button type="button" className={`erp-menu-item ${pestana === 'usuarios' ? 'active' : ''}`} onClick={() => navegarDesdeMenu('usuarios')}><span aria-hidden="true">♙</span> Usuarios</button>
              <button type="button" className={`erp-menu-item ${pestana === 'auditoria' ? 'active' : ''}`} onClick={() => navegarDesdeMenu('auditoria')}><span aria-hidden="true">◷</span> Auditoría</button>
            </div>}
            <div className="erp-menu-group erp-menu-session">
              <span className="erp-menu-group-title">SESIÓN</span>
              <button type="button" className="erp-menu-item erp-menu-logout" onClick={salirDesdeMenu}><span aria-hidden="true">↪</span> Cerrar sesión</button>
            </div>
          </div>
        </details>
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
        {pestana === 'venta' && puedeRegistrarVenta && <div className="erp-workspace">
          <aside className="erp-sidebar">
            <div className="sidebar-heading"><span>NETO</span><strong>EXPRESS</strong></div>
            <p className="sidebar-label">MÓDULO DE VENTAS</p>
            {puedeRegistrarVenta && <button className="side-link active" onClick={() => document.getElementById('registro')?.scrollIntoView({ behavior: 'smooth' })}><span className="side-icon">＋</span><span>Nueva venta</span><b>›</b></button>}
            <button className="side-link" onClick={() => setPestana('historial')}><span className="side-icon">▤</span><span>Tickets cobrados</span><b className="side-count">{totalTransacciones}</b></button>
            <div className="sidebar-catalog-hint"><strong>{productos.length} productos</strong><span>en catálogo</span><strong>{sucursales.length} sucursales</strong><span>disponibles</span></div>
            <div className="sidebar-profile"><span className="profile-mini">{usuarioActual.initials?.slice(0, 1) ?? 'U'}</span><div><small>SESIÓN ACTIVA</small><strong>{usuarioActual.roleLabel}</strong></div></div>
          </aside>

          <div className="erp-main-content">
            <section id="registro" className="content-card capture-card">
              <div className="section-title"><div className="title-with-mark"><span className="title-mark">+</span><div><p className="eyebrow">PUNTO DE VENTA</p><h2>{enEdicion ? 'Editar venta' : 'Registrar una venta'}</h2></div></div>
                <span className="live-indicator"><i /> Listo para operar</span></div>
              <FormularioVenta key={enEdicion?.id ?? 'nueva'} ventaEnEdicion={enEdicion} productos={productos} sucursales={sucursales} sucursalActual={sucursalActual} onSucursalChange={setSucursalActual} onGuardar={guardar} onCancelar={() => setEnEdicion(null)} />
            </section>
            <div className="quick-tip"><span>i</span><p>Agrega al carrito todos los productos del cliente y confirma el total una sola vez.</p></div>
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
          <Paginacion total={ventasFiltradas.length} pagina={paginaActual} onCambiar={setPaginaVentas} tamano={ventasPorPagina} etiqueta="ventas" />
        </section>}

        {/* Métricas agregadas y comparativo de sucursales. */}
        {pestana === 'resumen' && <section className="summary-page">
          <div className="metrics-grid" aria-label="Resumen de ventas">
            <article className="metric-card"><span className="metric-icon orange-icon">$</span><div><small>VENTA ACUMULADA</small><strong>{moneda.format(totalVentas)}</strong><span className="metric-note">Suma de operaciones</span></div></article>
            <article className="metric-card"><span className="metric-icon blue-icon">▤</span><div><small>TRANSACCIONES</small><strong>{totalTransacciones}</strong><span className="metric-note">Tickets cobrados</span></div></article>
            <article className="metric-card"><span className="metric-icon pale-icon">◷</span><div><small>PROMEDIO POR VENTA</small><strong>{moneda.format(promedioVenta)}</strong><span className="metric-note">Por registro</span></div></article>
          </div>
          <section className="content-card history-card branch-summary-card"><div className="section-title"><div><p className="eyebrow">COMPARATIVO</p><h2>Actividad por sucursal</h2></div><span className="records-count">{sucursales.length} sucursales</span></div>
            <label className="catalog-search">Buscar sucursal<input className="form-control" type="search" value={busquedaResumen} onChange={(e) => { setBusquedaResumen(e.target.value); setPaginaResumen(1) }} placeholder="Nombre de sucursal" /></label>
            <div className="table-responsive"><table className="table align-middle"><thead><tr><th>Sucursal</th><th className="text-end">Transacciones</th><th className="text-end">Venta acumulada</th></tr></thead><tbody>
              {filasResumenPagina.map((item) => <tr key={item.id}><td className="fw-semibold">{item.nombre}</td><td className="text-end">{item.cantidad}</td><td className="text-end fw-bold">{moneda.format(item.total)}</td></tr>)}
              {!sucursalesResumenFiltradas.length && <tr><td colSpan="3" className="text-center text-muted py-4">No hay sucursales que coincidan con la busqueda.</td></tr>}
            </tbody></table></div>
            <Paginacion total={sucursalesResumenFiltradas.length} pagina={paginaResumenActual} onCambiar={setPaginaResumen} etiqueta="sucursales" />
          </section>
        </section>}

        {/* Mantenimiento de los catálogos de productos y sucursales. */}
        {pestana === 'catalogos' && <GestionCatalogos productos={productos} sucursales={sucursales} puedeAdministrar={puedeGestionarCatalogos} cargandoProductos={cargandoProductos} errorProductos={errorProductos} onRecargarCatalogos={cargarCatalogos} onBuscarProducto={obtenerProducto} onCrearProducto={guardarProducto} onActualizarProducto={actualizarProductoCatalogo} onCrearSucursal={guardarSucursal} />}
        {pestana === 'usuarios' && puedeAdministrar && <AdministracionUsuarios usuarios={usuarios} cargando={cargandoUsuarios} error={errorUsuarios} onCrear={guardarUsuario} onActualizar={actualizarUsuarioAdmin} />}
        {/* Bitácora restringida visualmente al perfil encargado. */}
        {pestana === 'auditoria' && puedeAdministrar && <TablaAuditoria registros={auditoria} cargando={cargandoAuditoria} error={errorAuditoria} />}
        <footer className="page-footer"><span>NETO VENTAS</span><span>Administración de sucursal · MXN</span></footer>
      </main>
    </div>
  )
}

export default Aplicacion

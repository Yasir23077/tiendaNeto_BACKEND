// Pantalla para buscar, crear y editar cuentas, disponible solo para el encargado.
import { useState } from 'react'
import Paginacion from './Paginacion'

const roles = {
  CAJERO: 'Cajero',
  LIDER: 'Lider de tienda',
  ENCARGADO: 'Encargado de tienda',
}

// Función: Componente para que el encargado busque, cree y edite las cuentas del sistema.
export default function AdministracionUsuarios({ usuarios, cargando, error, onCrear, onActualizar }) {
  // Mantiene los valores del formulario de alta.
  const [nombre, setNombre] = useState('')
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [rol, setRol] = useState('CAJERO')
  const [guardando, setGuardando] = useState(false)
  const [pagina, setPagina] = useState(1)
  const [busqueda, setBusqueda] = useState('')
  const [activo, setActivo] = useState(true)
  const [usuarioEnEdicion, setUsuarioEnEdicion] = useState(null)

  // Busca por nombre, cuenta, rol o estado sin cambiar los datos originales.
  const usuariosFiltrados = usuarios.filter((usuario) =>
    [usuario.nombre, usuario.username, usuario.rol, usuario.activo ? 'activo' : 'inactivo']
      .some((valor) => String(valor ?? '').toLocaleLowerCase().includes(busqueda.trim().toLocaleLowerCase())))
  // Presenta hasta diez cuentas por pagina luego de aplicar la busqueda.
  const totalPaginas = Math.max(1, Math.ceil(usuariosFiltrados.length / 10))
  // Ajusta la página activa si el filtro deja menos resultados.
  const paginaActual = Math.min(pagina, totalPaginas)
  const usuariosPagina = usuariosFiltrados.slice((paginaActual - 1) * 10, paginaActual * 10)

  // Carga los datos de la cuenta elegida en el formulario.
  // Función: Carga una cuenta seleccionada en el formulario.
  const editar = (usuario) => {
    setUsuarioEnEdicion(usuario)
    setNombre(usuario.nombre)
    setUsername(usuario.username)
    setPassword('')
    setRol(usuario.rol)
    setActivo(usuario.activo)
  }

  // Función: Restablece el formulario.
  const limpiarFormulario = () => {
    setUsuarioEnEdicion(null)
    setNombre('')
    setUsername('')
    setPassword('')
    setRol('CAJERO')
    setActivo(true)
  }

  // Envia los datos al backend y limpia el formulario si se creo la cuenta.
  // Función: Valida y envía el formulario para crear o modificar el registro.
  const guardar = async (evento) => {
    evento.preventDefault()
    setGuardando(true)
    try {
      const datos = {
        nombre: nombre.trim(),
        username: username.trim().toLowerCase(),
        ...(usuarioEnEdicion ? { rol, activo, nuevaContrasena: password } : { password, rol }),
      }
      const guardado = usuarioEnEdicion
        ? await onActualizar(usuarioEnEdicion.id, datos)
        : await onCrear(datos)
      if (guardado) {
        limpiarFormulario()
        setPagina(1)
      }
    } finally {
      setGuardando(false)
    }
  }

  // Divide la vista entre formulario de edición y tabla de cuentas existentes.
  return (
    <div className="catalog-grid users-management">
      <section className="content-card catalog-card">
        <div className="section-title">
          <div><p className="eyebrow">ACCESO AL SISTEMA</p><h2>{usuarioEnEdicion ? 'Editar usuario' : 'Agregar usuario'}</h2></div>
        </div>
        <p className="catalog-readonly">{usuarioEnEdicion ? 'Actualiza el nombre, acceso, rol o estado. Deja la contrasena vacia para conservarla.' : 'Crea una cuenta y asigna el perfil que necesita para realizar su trabajo.'}</p>
        <form className="users-form" onSubmit={guardar}>
          <div>
            <label className="form-label" htmlFor="usuario-nombre">Nombre completo</label>
            <input id="usuario-nombre" className="form-control" value={nombre} onChange={(e) => setNombre(e.target.value)} maxLength={120} autoComplete="name" required />
          </div>
          <div>
            <label className="form-label" htmlFor="usuario-username">Nombre de usuario</label>
            <input id="usuario-username" className="form-control" value={username} onChange={(e) => setUsername(e.target.value)} minLength={3} maxLength={60} pattern="[A-Za-z0-9._-]{3,60}" autoComplete="username" required />
            <small>3 a 60 letras, numeros, punto, guion o guion bajo.</small>
          </div>
          <div>
            <label className="form-label" htmlFor="usuario-password">Contrasena inicial</label>
            <input id="usuario-password" type="password" className="form-control" value={password} onChange={(e) => setPassword(e.target.value)} minLength={10} maxLength={72} autoComplete="new-password" required={!usuarioEnEdicion} />
            <small>{usuarioEnEdicion ? 'Opcional. Si la cambias, debe tener entre 10 y 72 caracteres.' : 'Debe tener entre 10 y 72 caracteres.'}</small>
          </div>
          <div>
            <label className="form-label" htmlFor="usuario-rol">Rol</label>
            <select id="usuario-rol" className="form-select" value={rol} onChange={(e) => setRol(e.target.value)}>
              <option value="CAJERO">Cajero</option>
              <option value="LIDER">Lider de tienda</option>
              <option value="ENCARGADO">Encargado de tienda</option>
            </select>
          </div>
          {usuarioEnEdicion && <div>
            <label className="form-label" htmlFor="usuario-activo">Estado de la cuenta</label>
            <select id="usuario-activo" className="form-select" value={activo ? 'true' : 'false'} onChange={(e) => setActivo(e.target.value === 'true')}>
              <option value="true">Activa</option><option value="false">Inactiva</option>
            </select>
          </div>}
          <div className="users-form-actions">
            <button className="btn btn-neto" type="submit" disabled={guardando}>
              {guardando ? 'Guardando...' : usuarioEnEdicion ? 'Guardar cambios' : 'Crear usuario'}
            </button>
            {usuarioEnEdicion && <button className="btn btn-outline-secondary" type="button" onClick={limpiarFormulario} disabled={guardando}>Cancelar</button>}
          </div>
        </form>
      </section>

      <section className="content-card history-card users-list-card">
        <div className="section-title">
          <div><p className="eyebrow">CUENTAS REGISTRADAS</p><h2>Usuarios del sistema</h2></div>
          <span className="records-count">{usuarios.length} cuentas</span>
        </div>
        <label className="catalog-search">Buscar usuario<input className="form-control" type="search" value={busqueda} onChange={(e) => { setBusqueda(e.target.value); setPagina(1) }} placeholder="Nombre, usuario, rol o estado" /></label>
        {error && <div className="alert alert-danger" role="alert">{error}</div>}
        {cargando ? <div className="audit-empty">Cargando usuarios...</div> : <>
          <div className="table-responsive">
            <table className="table align-middle">
              <thead><tr><th>Nombre</th><th>Usuario</th><th>Rol</th><th>Estado</th><th>Acciones</th></tr></thead>
              <tbody>
                {usuariosPagina.map((usuario) => <tr key={usuario.id}>
                  <td className="fw-semibold">{usuario.nombre}</td>
                  <td>{usuario.username}</td>
                  <td>{roles[usuario.rol] || usuario.rol}</td>
                  <td><span className={`badge ${usuario.activo ? 'text-bg-success' : 'text-bg-secondary'}`}>{usuario.activo ? 'Activo' : 'Inactivo'}</span></td>
                  <td><button className="btn btn-outline-dark btn-sm" type="button" onClick={() => editar(usuario)}>Editar</button></td>
                </tr>)}
                {!usuariosFiltrados.length && <tr><td colSpan="5" className="text-center text-muted py-4">{usuarios.length ? 'No hay usuarios que coincidan con la busqueda.' : 'Aun no hay cuentas registradas.'}</td></tr>}
              </tbody>
            </table>
          </div>
          <Paginacion total={usuariosFiltrados.length} pagina={paginaActual} onCambiar={setPagina} etiqueta="usuarios" />
        </>}
      </section>
    </div>
  )
}
